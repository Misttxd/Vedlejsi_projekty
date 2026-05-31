#define _USE_MATH_DEFINES

#include <iostream>
#include <cmath>
#include <memory>

using namespace std;

class Tvar
{
public:
    virtual double Obsah() const = 0;
    virtual double Obvod() const = 0;
    virtual ~Tvar() {}
};

class Kruh : public Tvar
{
private:
    double polomer;

public:
    Kruh(double r) : polomer(r) {}

    double Obsah() const override {
        return M_PI * polomer * polomer;
    }

    double Obvod() const override {
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

    double Obsah() const override {
        return sirka * vyska;
    }

    double Obvod() const override {
        return 2 * (sirka + vyska);
    }

};

class Ctverec : public Obdelnik
{
public:
    Ctverec(double a) : Obdelnik(a, a) {}

};

int main()
{
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


    return 0;
}
