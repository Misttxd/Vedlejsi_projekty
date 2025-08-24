#include <iostream>

using namespace std;

class Client
{
private:
    int code;
    string name;

public:
    Client(int c, string n)
    {
        code = c;
        name = n;
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
    int number;
    double balance;
    double interestRate;

    Client *owner;
    Client *partner;

public:
    Account(int n, Client *c)
    {
        this->number = n;
        this->balance = 0;
        this->interestRate = 0.0;
        this->owner = c;
        this->partner = nullptr;
    }

    Account(int n, Client *c, double ir)
    {
        this->number = n;
        this->balance = 0;
        this->interestRate = ir;
        this->owner = c;
        this->partner = nullptr;
    }

    Account(int n, Client *c, Client *p)
    {
        this->number = n;
        this->balance = 0;
        this->interestRate = 0.0;
        this->owner = c;
        this->partner = p;
    }
    Account(int n, Client *c, Client *p, double ir)
    {
        this->number = n;
        this->balance = 0;
        this->interestRate = ir;
        this->owner = c;
        this->partner = p;
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

class Student
{
private:
    int id;
    string name;

public:
    Student(int id, string name)
    {
        this->id = id;
        this->name = name;
    }

    int GetID()
    {
        return this->id;
    }

    string GetName()
    {
        return this->name;
    }
};

class Grades
{
private:
    int gradeCount;
    Student *student;
    int *grades;

public:
    Grades(Student *student, int *inputGrades, int gradeCount)
    {
        this->student = student;
        this->gradeCount = gradeCount;

        this->grades = new int[gradeCount];
        for (int i = 0; i < gradeCount; i++)
        {
            this->grades[i] = inputGrades[i];
        }
    }

    ~Grades()
    {
        delete[] grades;
    }

    Student *GetStudent()
    {
        return this->student;
    }

    int GetGrade(int index)
    {
        if (index >= 0 && index < gradeCount)
        {
            return grades[index];
        }

        else
        {
            return -1;
        }
    }

    void AddGrade(int newGrade)
    {
        int *newGrades = new int[gradeCount + 1];
        for (int i = 0; i < gradeCount; i++)
        {
            newGrades[i] = grades[i];
        }
        newGrades[gradeCount] = newGrade;
        gradeCount++;

        delete[] grades; // Správně uvolníme staré pole
        grades = newGrades;
    }
};

class School
{
private:
    Student **students;
    int studentsCount;

    Grades **grades;
    int gradesCount;

public:
    School(int maxStudents, int maxGrades) // šlo by udělat jenom jako max student, co by bylo pro oboje
    {
        this->studentsCount = 0;
        this->gradesCount = 0;

        this->students = new Student *[maxStudents]; // udává maximální počet klientů
        this->grades = new Grades *[maxGrades];      // udává maximální počet účtů
    }
    ~School()
    {
        for (int i = 0; i < studentsCount; i++)
        {
            delete this->students[i];
        }
        delete[] this->students;

        for (int i = 0; i < gradesCount; i++)
        {
            delete this->grades[i];
        }
        delete[] this->grades;
    }

    Student *GetStudent(int id)
    {
        for (int i = 0; i < studentsCount; i++)
            if (students[i]->GetID() == id)
                return students[i];

        return nullptr;
    }
    Grades *GetGrade(Student *student)
    {
        for (int i = 0; i < gradesCount; i++)
        {
            if (grades[i]->GetStudent() == student)
                return grades[i];
        }

        return nullptr;
    }

    Student *CreateStudent(int id, string name)
    {
        students[studentsCount] = new Student(id, name);
        return students[studentsCount++];
    }
    Grades *CreateGrades(Student *student, int *inputGrades, int count)
    {
        grades[gradesCount] = new Grades(student, inputGrades, count);
        return grades[gradesCount++];
    }
};

int main()
{
    Bank bank(10, 10);

    Client *c1 = bank.CreateClient(1, "Adam");
    Account *a1 = bank.CreateAccount(1, c1, 3.14);

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

    bank.AddInterest();

    cout << "Hodnoty po zuroceni (" << a1->GetInterest() << "%):" << endl;
    cout << "Ucet " << c1->GetCode() << " - stav: " << a1->GetBalance() << endl;
    cout << endl;

    // Vytvoření nového účtu
    Client *c2 = bank.CreateClient(2, "Anna");
    Account *a2 = bank.CreateAccount(2, c2, 2);

    a1->SendMoney(a2, 20000);

    cout << "Hodnoty po poslani:" << endl;
    cout << "Ucet " << c1->GetCode() << " - stav: " << a1->GetBalance() << endl;
    cout << "Ucet " << c2->GetCode() << " - stav: " << a2->GetBalance() << endl;
    cout << endl;

    // Vytvoreni desitek dalsich uctu

    Client *c3 = bank.CreateClient(3, "Pdwad");
    Account *a3 = bank.CreateAccount(3, c3, 3.14);

    Client *c4 = bank.CreateClient(4, "ADadw");
    Account *a4 = bank.CreateAccount(4, c4, 3.14);

    Client *c5 = bank.CreateClient(5, "fggew");
    Account *a5 = bank.CreateAccount(5, c5, 3.14);

    Client *c6 = bank.CreateClient(6, "dasdas");
    Account *a6 = bank.CreateAccount(6, c6, 3.14);

    Client *c7 = bank.CreateClient(7, "gfdgd");
    Account *a7 = bank.CreateAccount(7, c7, 3.14);

    Client *c8 = bank.CreateClient(8, "hgfh");
    Account *a8 = bank.CreateAccount(8, c8, 3.14);

    Client *c9 = bank.CreateClient(9, "cxvx");
    Account *a9 = bank.CreateAccount(9, c9, 3.14);

    Client *c10 = bank.CreateClient(10, "ztr");
    Account *a10 = bank.CreateAccount(10, c10, 3.14);

    School school(10, 10);
    Student *s1 = school.CreateStudent(1, "Andreas");
    int znamky[] = {1, 1, 1, 3, 2, 3};
    int pocetZnamek = sizeof(znamky) / sizeof(znamky[0]);
    Grades *g1 = school.CreateGrades(s1, znamky, pocetZnamek);

    cout << "Student: " << g1->GetStudent()->GetName() << endl;
    for (int i = 0; i < pocetZnamek; i++)
    {
        cout << i + 1 << ". znamka: " << g1->GetGrade(i) << endl;
    }

    return 0;
}