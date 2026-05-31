#define _USE_MATH_DEFINES

#include <iostream>
#include <cmath>
#include <memory>

using namespace std;

class Tvar // èistì abstraktní tøída
{
public:
    virtual double Obsah() = 0;
    virtual double Obvod() = 0;
    virtual ~Tvar() {}
};

class Kruh : public Tvar
{
private:
    double polomer;

public:
    Kruh(double r) : polomer(r) {}

    virtual double Obsah() //virtual metody (u každé)
    {
        return M_PI * polomer * polomer;
    }

    virtual double Obvod() 
    {
        return 2 * M_PI * polomer;
    }

};

class Obdelnik : public Tvar
{
protected:
    double sirka;
    double vyska;

public:
    Obdelnik(double a, double b) : sirka(a), vyska(b) {}

    virtual double Obsah()
    {
        return sirka * vyska;
    }

    virtual double Obvod()
    {
        return 2 * (sirka + vyska);
    }

};

class Ctverec : public Obdelnik
{
public:
    Ctverec(double a) : Obdelnik(a, a) {}

};


int main() {
    const int N = 3;
    Tvar* tvary[N];

    tvary[0] = new Kruh(5.0);
    tvary[1] = new Obdelnik(4.0, 6.0);
    tvary[2] = new Ctverec(5.0);

    for (int i = 0; i < N; ++i) {
        cout << "Obsah: " << tvary[i]->Obsah() << endl;
        cout << "Obvod: " << tvary[i]->Obvod() << endl << endl;
    }

    for (int i = 0; i < N; ++i) {
        delete tvary[i];
    }

    return 0;
}
