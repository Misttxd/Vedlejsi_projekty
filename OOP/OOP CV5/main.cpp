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
    double balance;
    double interestRate;

    static double baseInterestRate;

    Client* owner;

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
    //TADY --


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
    


    bool CanWithdraw(double amount)
    {
        if (this->balance > amount)
        {
            return 1;
        }
        else if (this->balance < amount)
        {
            return 0;
        }
    }

    void Deposit(double amount)
    {
        balance = balance + amount;
    }
    bool Withdraw(double amount)
    {
        if (CanWithdraw(amount) == 1) {
            balance = balance - amount;
            return 1;
        }
        else
        {
            return 0;
        }
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


//TOHLE CELE
class PartnerAccount : public Account
{
private:
    Client* partner;

public:
    PartnerAccount(int n, Client *o, Client *p);
    PartnerAccount(int n, Client *o, Client *p, double ir);

    Client* GetPartner()
    {
        return this->partner;
    }
};

//TOHLE
PartnerAccount::PartnerAccount(int n, Client* o, Client* p) : Account(n, o)
{
    this->partner = p;
}

//TOHLE
PartnerAccount::PartnerAccount(int n, Client* o, Client *p,  double ir) : Account(n, o, ir)
{
    this->partner = p;
}

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
    /*Bank bank(10, 10);

    Client* c1 = bank.CreateClient(1, "Adam");
    Account* a1 = bank.CreateAccount(1, c1, 3.14);

    Client* c2 = bank.CreateClient(2, "Anna");
    Account* a2 = bank.CreateAccount(2, c2);

    cout << "Puvodni hodnoty uctu" << endl;
    cout << "Ucet " << c1->GetCode() << " - stav: " << a1->GetBalance() << endl;
    cout << endl;

    a1->Deposit(500);

    cout << "Hodnoty po vlozeni" << endl;
    cout << "Ucet " << c1->GetCode() << " - stav: " << a1->GetBalance() << endl;
    cout << endl;

    a1->Withdraw(200);

    cout << "Hodnoty po vybrani" << endl;
    cout << "Ucet " << c1->GetCode() << " - stav: " << a1->GetBalance() << endl;
    cout << endl;

    a1->SendMoney(a2, 200);

    cout << "Hodnoty po poslani:" << endl;
    cout << "Ucet " << c1->GetCode() << " - stav: " << a1->GetBalance() << endl;
    cout << "Ucet " << c2->GetCode() << " - stav: " << a2->GetBalance() << endl;
    cout << endl;


    bank.AddInterest();

    cout << "Hodnoty po zuroceni (" << a1->GetInterest() << "%):" << endl;
    cout << "Ucet " << c1->GetCode() << " - stav: " << a1->GetBalance() << endl;
    cout << "Hodnoty po zuroceni (" << a2->GetInterest() << "%):" << endl; 
    cout << "Ucet " << c2->GetCode() << " - stav: " << a2->GetBalance() << endl;
    cout << endl;



    Account::SetBaseInterestRate(20.0);
    Client* c3 = bank.CreateClient(3, "BaseInterestRate");
    Account* a3 = bank.CreateAccount(3, c3);

    a3->Deposit(1000);
    cout << "Hodnoty po vlozeni" << endl;
    cout << "Ucet " << c3->GetCode() << " - stav: " << a3->GetBalance() << endl;
    cout << endl;
    bank.AddInterest();

    cout << "Hodnoty po zuroceni (" << a3->GetInterest() << "%):" << endl;
    cout << "Ucet " << c3->GetCode() << " - stav: " << a3->GetBalance() << endl;

    cout << endl;
    */


    Account* a;
    Account* pa;

    Bank* b = new Bank(100, 1000);
    Client* o = b->CreateClient(0, "Smith");
    Client* p = b->CreateClient(1, "Jones");
    a = b->CreateAccount(0, o);
    pa = b->CreateAccount(1, o, p);

    cout << a->GetOwner()->GetName() << endl;
    cout << pa->GetOwner()->GetName() << endl;

    cout << b->GetClient(1)->GetName() << endl;
    //cout << b->GetClient(1)->GetPartner() << endl;



    return 0;
}