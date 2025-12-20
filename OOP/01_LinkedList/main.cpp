#include <iostream>
using namespace std;

class KeyValue
{
private:
    int key;
    double value;
    KeyValue *next;

public:
    KeyValue(int k, double v)
    {
        this->key = k;
        this->value = v;
        this->next = nullptr;
    }
    ~KeyValue()
    {
        if (this->next != nullptr)
        {
            delete this->next;
            this->next = nullptr;
        }
    }

    int GetKey()
    {
        return this->key;
    }
    double GetValue()
    {
        return this->value;
    }
    KeyValue *GetNext()
    {
        return this->next;
    }

    KeyValue *CreateNext(int k, double v)
    {
        this->next = new KeyValue(k, v);
        return this->next;
    }
};

void CreateAndPrintLinkedList(int size)
{
    KeyValue *head = new KeyValue(1, 1.1); // První prvek seznamu
    KeyValue *current = head;

    // Vytvoření zbývajících prvků
    for (int i = 2; i <= size; ++i)
    {
        current = current->CreateNext(i, i + 0.1);
    }

    // Výpis všech klíčů
    current = head;
    while (current)
    {
        cout << "Key: " << current->GetKey() << endl;
        current = current->GetNext();
    }

    // Správné uvolnění paměti
    delete head;
}

class Tree
{
private:
    string key;
    string value;
    Tree *nextL;
    Tree *nextR;

public:
    Tree(string k, string v)
    {
        this->key = k;
        this->value = v;
        this->nextL = nullptr;
        this->nextR = nullptr;
    }

    ~Tree()
    {
        delete nextL;
        delete nextR;
    }

    string GetKey()
    {
        return this->key;
    }
    string GetValue()
    {
        return this->value;
    }
    Tree *GetNextL()
    {
        return this->nextL;
    }
    Tree *GetNextR()
    {
        return this->nextR;
    }

    Tree *CreateNextL(string k, string v)
    {
        this->nextL = new Tree(k, v);
        return this->nextL;
    }

    Tree *CreateNextR(string k, string v)
    {
        this->nextR = new Tree(k, v);
        return this->nextR;
    }

    void PrintTree(int depth = 0)
    {
        for (int i = 0; i < depth; ++i)
            cout << "  "; // Odsazení podle hloubky
        cout << "Key: " << key << ", Value: " << value << endl;
        if (nextL)
            nextL->PrintTree(depth + 1);
        if (nextR)
            nextR->PrintTree(depth + 1);
    }
};

int main()
{
    /*KeyValue* kv1 = new KeyValue(1, 1.5);
    cout << kv1->CreateNext(2, 2.5)->GetKey() << endl;

    KeyValue* kv2 = kv1->GetNext();
    cout << kv2->GetNext() << endl;

    delete kv1;
    kv1 = nullptr;
    kv2 = nullptr;

    //cout << kv1->GetKey() << endl;
    //cout << kv2->GetKey() << endl;*/

    CreateAndPrintLinkedList(1000);

    // Vytvoreni korenoveho uzlu s pocatecni otazkou
    Tree *root = new Tree("Ma to kridla?", "Nezname zvire");

    // Pridani leveho a praveho potomka
    Tree *left = root->CreateNextL("Ma peri?", "Ptak");
    Tree *right = root->CreateNextR("Je to suchozemske?", "Had");

    // Dalsi uroven stromu (podrobnosti o ptacich)
    left->CreateNextL("Je to velky ptak?", "Orel");
    left->CreateNextR("Je to maly ptak?", "Sykočka");

    // Dalsi uroven stromu (podrobnosti o hadech)
    right->CreateNextL("Je to had?", "Had");
    right->CreateNextR("Je to jezek?", "Jezek");

    // Vypis celeho stromu
    root->PrintTree();

    // Uvolneni pameti
    delete root;

    getchar();

    return 0;
}