#include <iostream>

using namespace std;

class Auto 
{
private:
    string znacka;
    int rokVyroby;
    
    static int autoCount;


public:
    Auto(string znacka, int rokVyroby)
    {
        this->znacka = znacka;
        this->rokVyroby = rokVyroby;

        autoCount = autoCount + 1;
    }

    ~Auto()
    {
        autoCount = autoCount - 1;
    }

    int getRokVyroby()
    {
        return this->rokVyroby;

    }

    string getZnacka()
    {
        return this->znacka;
    }

    static int getAutoCount()
    {
        return autoCount;
    }

};

class sportovniAuto : public Auto
{
private:
    double z0na100;
    double maxRychlost;
    int vykon;

public:
    sportovniAuto(string znacka, int rokVyroby, double z0na100, double maxRychlost, int vykon) : Auto(znacka, rokVyroby)
    {
        this->z0na100 = z0na100;
        this->maxRychlost = maxRychlost;
        this->vykon = vykon;
    }

};

class osobniAuto : public Auto
{
private:
    string karoserie;
    int pocetSedacek;

public:
    osobniAuto(string znacka, int rokVyroby, string karoserie, int pocetSedacek) : Auto(znacka, rokVyroby)
    {
        this->karoserie = karoserie;
        this->pocetSedacek = pocetSedacek;
    }
};

class Garaz
{
private:
    Auto** auta;
    int maxKapacita;
    int pocetAut;

public:
    Garaz(int maxKapacita)
    {
        this->maxKapacita = maxKapacita;
        this-> pocetAut = 0;
        this->auta = new Auto * [maxKapacita];
    }

    ~Garaz()
    {
        for (int i = 0; i < pocetAut; i++) {
            delete auta[i];
        }
        delete[] auta;
    }

    int getPocetAut()
    {
        return this->pocetAut;
    }

    bool PridatAuto(Auto* a)
    {
        if (pocetAut < maxKapacita)
        {
            auta[pocetAut] = a;
            pocetAut++;
            return 1;
        }
        else
        {
            cout << "Garáž je plná!" << endl;
            return 0;
        }
    }

    void VypisAuta()
    {
        cout << "V garazi je " << pocetAut << " aut:" << endl;
        for (int i = 0; i < pocetAut; i++) {
            cout <<i +1<<". auto: " << auta[i]->getZnacka() << endl; // Vytiskne znaèku auta
        }
    }
 };

int Auto::autoCount = 0;

int main()
{
    Garaz* g = new Garaz(5);

    g->PridatAuto(new osobniAuto("Citroen", 2009, "hatchback", 5));
    g->PridatAuto(new sportovniAuto("Aston Martin", 2019, 2.9, 340, 670));

    g->VypisAuta();

    cout << "Pocet aut v garazi: " << g->getPocetAut() << endl;
    cout << "Pocet aut celkem: " << Auto::getAutoCount() << endl;

    delete g;

    return 0;
}