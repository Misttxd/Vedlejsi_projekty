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

    static double baseInterestRate; // tohle

    Client *owner;
    Client *partner;

public:
    Account(int n, Client *c)
    {
        this->number = n;
        this->balance = 0;
        this->interestRate = baseInterestRate; // tady (puvodne byla 0.0)
        this->owner = c;
        this->partner = nullptr;

        Account::objectsCount = +1;
    }

    Account(int n, Client *c, double ir)
    {
        this->number = n;
        this->balance = 0;
        this->interestRate = ir;
        this->owner = c;
        this->partner = nullptr;

        Account::objectsCount = +1;
    }

    Account(int n, Client *c, Client *p)
    {
        this->number = n;
        this->balance = 0;
        this->interestRate = baseInterestRate; // tady
        this->owner = c;
        this->partner = p;

        Account::objectsCount = +1;
    }
    Account(int n, Client *c, Client *p, double ir)
    {
        this->number = n;
        this->balance = 0;
        this->interestRate = ir;
        this->owner = c;
        this->partner = p;

        Account::objectsCount += 1;
    }

    ~Account()
    {
        Account::objectsCount -= 1; // tohle
    }

    static void SetBaseInterestRate(double IR)
    {
        Account::baseInterestRate = IR; // tohle
    }

    static double GetBaseInterestRate()
    {
        return baseInterestRate; // tohle
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
    Client *GetOwner()
    {
        return this->owner;
    }
    Client *GetPartner()
    {
        return this->partner;
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
        if (CanWithdraw(amount) == 1)
        {
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

    bool SendMoney(Account *reciever, double amount)
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

class Bank
{
private:
    Client **clients;
    int clientsCount;

    Account **accounts;
    int accountsCount;

public:
    Bank(int c, int a)
    {
        this->clientsCount = 0;
        this->accountsCount = 0;

        this->clients = new Client *[c];   // udává maximální počet klientů
        this->accounts = new Account *[a]; // udává maximální počet účtů
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

    Client *GetClient(int c) // protoze to proste nemuze byt jednoduse return clients[c] ...;
    {
        for (int i = 0; i < clientsCount; i++)
        {
            if (clients[i]->GetCode() == c)
            {
                return clients[i];
            }
        }
        return nullptr;
    }
    Account *GetAccount(int n)
    {
        for (int i = 0; i < accountsCount; i++)
        {
            if (accounts[i]->GetNumber() == n)
            {
                return accounts[i];
            }
        }
        return nullptr;
    }

    Client *CreateClient(int c, string n)
    {
        clients[clientsCount] = new Client(c, n);
        clientsCount++;
        return clients[clientsCount - 1];
    }
    Account *CreateAccount(int n, Client *c)
    {
        accounts[accountsCount] = new Account(n, c);
        accountsCount++;
        return accounts[accountsCount - 1];
    }
    Account *CreateAccount(int n, Client *c, double ir)
    {
        accounts[accountsCount] = new Account(n, c, ir);
        accountsCount++;
        return accounts[accountsCount - 1];
    }
    Account *CreateAccount(int n, Client *c, Client *p)
    {
        accounts[accountsCount] = new Account(n, c, p);
        accountsCount++;
        return accounts[accountsCount - 1];
    }
    Account *CreateAccount(int n, Client *c, Client *p, double ir)
    {
        accounts[accountsCount] = new Account(n, c, p, ir);
        accountsCount++;
        return accounts[accountsCount - 1];
    }

    void AddInterest()
    {
        for (int i = 0; i < accountsCount; i++)
        {
            accounts[i]->AddInterest();
        }
    }
};

int Client::objectsCount = 0;
int Account::objectsCount = 0;
double Account::baseInterestRate = 2.0;

int main()
{
    Bank bank(10, 10);

    Client *c1 = bank.CreateClient(1, "Adam");
    Account *a1 = bank.CreateAccount(1, c1, 3.14);

    Client *c2 = bank.CreateClient(2, "Anna");
    Account *a2 = bank.CreateAccount(2, c2);

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
    cout << "Hodnoty po zuroceni (" << a2->GetInterest() << "%):" << endl; // tohle
    cout << "Ucet " << c2->GetCode() << " - stav: " << a2->GetBalance() << endl;
    cout << endl;

    // tohle
    Account::SetBaseInterestRate(20.0);
    Client *c3 = bank.CreateClient(3, "BaseInterestRate");
    Account *a3 = bank.CreateAccount(3, c3);

    a3->Deposit(1000);
    cout << "Hodnoty po vlozeni" << endl;
    cout << "Ucet " << c3->GetCode() << " - stav: " << a3->GetBalance() << endl;
    cout << endl;
    bank.AddInterest();

    cout << "Hodnoty po zuroceni (" << a3->GetInterest() << "%):" << endl;
    cout << "Ucet " << c3->GetCode() << " - stav: " << a3->GetBalance() << endl;

    cout << endl;

    return 0;
}