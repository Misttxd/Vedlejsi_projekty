#include <iostream>
#include <vector>
using namespace std;

class KeyValue
{
private:
    int key;
    double value;

public:
    KeyValue(int k, double v)
    {
        this->key = k;
        this->value = v;
    }

    int GetKey()
    {
        return this->key;
    }
    double GetValue()
    {
        return this->value;
    }
};

class KeyValues
{
private:
    KeyValue **keyValues;
    int count;

public:
    KeyValues(int n)
    {
        this->keyValues = new KeyValue *[n];
        this->count = 0;
    }
    ~KeyValues()
    {
        for (int i = 0; i < this->count; i++)
        {
            delete this->keyValues[i];
        }
        delete[] this->keyValues;
    }

    KeyValue *CreateObject(int k, double v)
    {

        KeyValue *newObject = new KeyValue(k, v);

        this->keyValues[this->count] = newObject;
        this->count += 1;

        return newObject;
    }
    KeyValue *SearchObject(int k)
    {
        for (int i = 0; i < this->count; i++)
        {
            if (this->keyValues[i]->GetKey() == k)
            {
                return this->keyValues[i];
            }
        }

        return nullptr;
    }

    int Count()
    {
        return this->count;
    }

    KeyValue *RemoveObject(int k) // UKOL 1
    {
        int tmp_i = -1;
        KeyValue *removedPointer = nullptr;

        for (int i = 0; i < this->count; i++) // Projde vsechny objekty dokud se nenajde ten pravy
        {
            if (keyValues[i]->GetKey() == k)
            {
                tmp_i = i;

                break;
            }
        }

        if (tmp_i == -1)
        {
            return nullptr; // objekt se nenasel
        }

        removedPointer = keyValues[tmp_i];

        for (int i = tmp_i; i < this->count - 1; i++) // Posunuti hodnot ostatnich o jeden doleva
        {
            keyValues[i] = keyValues[i + 1];
        }

        this->count--;

        return removedPointer;
    }
};

class PolozkaFaktury
{
private:
    string nazev;
    double cena;

public:
    PolozkaFaktury(string nazev, double cena)
    {
        this->nazev = nazev;
        this->cena = cena;
    }

    string GetNazev()
    {
        return this->nazev;
    }
    double GetCena()
    {
        return this->cena;
    }
};

class Osoba
{
private:
    string jmeno;
    string adresa;

public:
    Osoba(string jmeno, string adresa)
    {
        this->jmeno = jmeno;
        this->adresa = adresa;
    }

    string GetJmeno()
    {
        return this->jmeno;
    }
    string GetAdresa()
    {
        return this->adresa;
    }
};

class Faktura
{
private:
    // const int p = 10;

    int cislo;
    Osoba *osoba;
    PolozkaFaktury **polozky;
    int pocetPolozek; // absolutne nevim jestli to bylo mysleno jako to cislo, ale takhle mi to sedi vice pro indexovani poctu polozek

public:
    Faktura(int cislo, Osoba *osoba)
    {
        this->cislo = cislo;
        this->osoba = osoba;
        this->pocetPolozek = 0;
        this->polozky = new PolozkaFaktury *[10]; // Pole na max. 10 položek
    }
    ~Faktura()
    {
        for (int i = 0; i < pocetPolozek; i++)
        {
            delete this->polozky[i];
        }
        delete[] this->polozky;
    }

    void PridatPolozku(string nazev, double cena)
    {
        if (pocetPolozek < 10)
        { // Kontrola, zda není pole plné
            polozky[pocetPolozek] = new PolozkaFaktury(nazev, cena);
            pocetPolozek++;
        }
        else
        {
            cout << "Nelze přidat další položku, faktura je plná!" << endl;
        }
    }

    int Count()
    {
        return this->pocetPolozek;
    }

    double CelkovaCena() const
    {
        double suma = 0;
        for (int i = 0; i < pocetPolozek; i++)
        {
            suma += polozky[i]->GetCena();
        }
        return suma;
    }

    void VypisFakturu() const
    {
        cout << "Faktura cislo: " << cislo << endl;
        cout << "Zakaznik: " << osoba->GetJmeno() << ", " << osoba->GetAdresa() << endl;
        cout << "Polozky:" << endl;
        for (int i = 0; i < pocetPolozek; i++)
        {
            cout << "- " << polozky[i]->GetNazev() << ": " << polozky[i]->GetCena() << " CZK" << endl;
        }
        cout << "Celkem: " << CelkovaCena() << " CZK" << endl;
    }
};

int main()
{
    /*int N = 5;
    KeyValues* myKeyValues = new KeyValues(N);

    // Vytvoření a výpis hodnoty prvního objektu
    KeyValue* myKeyValue = myKeyValues->CreateObject(0, 0.5);

    cout << myKeyValue->GetValue() << endl;

    // Vytvoření dalších objektů
    for (int i = 0; i < N; i++)
    {
        myKeyValues->CreateObject(i, i + 0.5);
    }

    // Vyhledání objektu s klíčem 4 a výpis jeho hodnoty
    cout << myKeyValues->SearchObject(4)->GetValue() << endl;


    // Testování metody RemoveObject a výpis výsledku
    KeyValue* removed = myKeyValues->RemoveObject(3);
    if (removed != nullptr) {
        cout << "Odstranen objekt " << removed->GetKey() << " s hodnotou: " << removed->GetValue() << endl;
    }
    else {
        cout << "Objekt neni :(" << endl;
    }

    // Vyčištění paměti
    delete myKeyValues;*/

    Osoba *zakaznik = new Osoba("Petr Pavel", "Ohio");
    Faktura *faktura = new Faktura(1, zakaznik);

    // Přidání položek do faktury
    faktura->PridatPolozku("polozka1", 123);
    faktura->PridatPolozku("polozka2", 456);
    faktura->PridatPolozku("polozka3", 959);
    faktura->PridatPolozku("ponozka4", 1);

    Osoba *zakaznik2 = new Osoba("ys", "prag");
    Faktura *faktura2 = new Faktura(2, zakaznik);

    // Přidání položek do faktury
    faktura->PridatPolozku("polozkadaa1", 11323);
    faktura->PridatPolozku("polozkaada2", 451236);
    faktura->PridatPolozku("polozkadada3", 952319);
    faktura->PridatPolozku("ponozkadada4", 13);

    // Výpis faktury
    faktura->VypisFakturu();

    // Uvolnění paměti
    delete faktura;
    delete zakaznik;

    getchar();

    return 0;
}