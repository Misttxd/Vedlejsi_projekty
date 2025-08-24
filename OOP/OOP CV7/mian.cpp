#include <iostream>

using namespace std;

class Client
{
private:
    static int objectsCount;
    int code;
    string name;

public:


    Client(int c, string n)
    {
        code = c;
        name = n;
        Client::objectsCount += 1;

    }
    ~Client()
    {
        Client::objectsCount -= 1;
    }

    static int GetObjectsCount()
    {
        return objectsCount;
    }


    int GetCode()
    {
        return this->code;
    }
    string GetName()
    {
        return this->name;
    }
};


class Account
{
private:
    static int objectsCount;
    int number;
    //tady byl balance
    double interestRate;
    static double baseInterestRate;

    Client* owner;

protected:
    double balance;

public:

    Account(int n, Client* o)
    {
        this->number = n;
        this->balance = 0;
        this->interestRate = baseInterestRate;
        this->owner = o;

        Account::objectsCount = +1;
    }

    Account(int n, Client* o, double ir)
    {
        this->number = n;
        this->balance = 0;
        this->interestRate = ir;
        this->owner = o;

        Account::objectsCount = +1;
    }

    ~Account()
    {
        Account::objectsCount -= 1;
    }

    static void SetBaseInterestRate(double IR)
    {
        Account::baseInterestRate = IR;
    }

    static double GetBaseInterestRate()
    {
        return baseInterestRate;
    }

    static int GetObjectsCount()
    {
        return objectsCount;
    }


    int GetNumber()
    {
        return this->number;
    }
    double GetBalance()
    {
        return this->balance;
    }
    double GetInterest()
    {
        return this->interestRate;
    }
    Client* GetOwner()
    {
        return this->owner;
    }



    virtual bool CanWithdraw(double amount)
    {
        return (this->balance >= amount);
    }

    void Deposit(double amount)
    {
        balance = balance + amount;
    }

    bool Withdraw(double amount)
    {
        bool success = false;
        if (this->CanWithdraw(amount))
        {
            this->balance -= amount;
            success = true;
        }
        return success;
    }
    void AddInterest()
    {

        balance = balance + (balance * interestRate / 100);
    }

    bool SendMoney(Account* reciever, double amount)
    {
        if (CanWithdraw(amount) == 1)
        {
            this->balance = this->balance - amount;

            reciever->balance = reciever->balance + amount;
            return 1;
        }

        else
        {
            cout << "Odesilatel nema na ucte potrebne prostredky pro vykonani transakce." << endl;
            return 0;
        }



    }
};

class PartnerAccount : public Account
{
private:
    Client* partner;

public:
    PartnerAccount(int n, Client* o, Client* p) : Account(n, o)
    {
        this->partner = p;
    }
    PartnerAccount(int n, Client* o, Client* p, double ir) : Account(n, o, ir)
    {
        this->partner = p;
    }

    Client* GetPartner()
    {
        return this->partner;
    }
};

class CreditAccount : public Account
{
private:
    double credit;

public:
    CreditAccount(int n, Client* o, double c) : Account(n, o)
    {
        this->credit = c;
    }
    CreditAccount(int n, Client* o, double ir, double c) : Account(n, o, ir)
    {
        this->credit = c;
    }

    bool CanWithdraw(double a)
    {
        return (this->GetBalance() + this->credit >= a);
    }
    bool WithDraw(double a)
    {
        bool success = false;
        if (this->CanWithdraw(a))
        {
            this->balance -= a;
            success = true;
        }
        return success;
    }
};


class Bank
{
private:
    Client** clients;
    int clientsCount;

    Account** accounts;

    int accountsCount;



public:
    Bank(int c, int a)
    {
        this->clientsCount = 0;
        this->accountsCount = 0;

        this->clients = new Client * [c];
        this->accounts = new Account * [a];
    }
    ~Bank()
    {
        for (int i = 0; i < clientsCount; i++)
        {
            delete this->clients[i];
        }
        delete[] this->clients;

        for (int i = 0; i < accountsCount; i++)
        {
            delete this->accounts[i];
        }
        delete[] this->accounts;
    }

    Client* GetClient(int c)
    {
        for (int i = 0; i < clientsCount; i++) {
            if (clients[i]->GetCode() == c) {
                return clients[i];
            }
        }
        return nullptr;
    }
    Account* GetAccount(int n)
    {
        for (int i = 0; i < accountsCount; i++) {
            if (accounts[i]->GetNumber() == n) {
                return accounts[i];
            }
        }
        return nullptr;
    }

    Client* CreateClient(int c, string n)
    {
        clients[clientsCount] = new Client(c, n);
        clientsCount++;
        return clients[clientsCount - 1];
    }
    Account* CreateAccount(int n, Client* c)
    {
        accounts[accountsCount] = new Account(n, c);
        accountsCount++;
        return accounts[accountsCount - 1];
    }
    Account* CreateAccount(int n, Client* c, double ir)
    {
        accounts[accountsCount] = new Account(n, c, ir);
        accountsCount++;
        return accounts[accountsCount - 1];
    }
    Account* CreateAccount(int n, Client* c, Client* p)
    {
        accounts[accountsCount] = new PartnerAccount(n, c, p); //tady
        accountsCount++;
        return accounts[accountsCount - 1];
    }
    Account* CreateAccount(int n, Client* c, Client* p, double ir)
    {
        accounts[accountsCount] = new PartnerAccount(n, c, p, ir); //tady
        accountsCount++;
        return accounts[accountsCount - 1];
    }

    void AddInterest()
    {
        for (int i = 0; i < accountsCount; i++) {
            accounts[i]->AddInterest();
        }
    }

};






int Client::objectsCount = 0;
int Account::objectsCount = 0;
double Account::baseInterestRate = 2.0;

int main2()
{
    Client* o = new Client(0, "Smith");

    CreditAccount* ca = new CreditAccount(1, o, 1000);
    cout << ca->CanWithdraw(1000) << endl;

    Account* a = ca;
    cout << a->CanWithdraw(1000) << endl;

    cout << ca->Withdraw(1000) << endl;

    a = nullptr;
    delete ca;
    delete a;

    getchar();
    return 0;
}