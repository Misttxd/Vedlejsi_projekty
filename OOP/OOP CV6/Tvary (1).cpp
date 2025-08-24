#define _USE_MATH_DEFINES

#include <iostream>
#include <math.h>
#include <cmath>

using namespace std;


class Tvar
{
public:
	double Obsah()
	{
		return 0;
	}
	double Obvod()
	{
		return 0;
	}
};

class Kruh : public Tvar
{
private:
	double polomer;

public:
	Kruh(double r)
	{
		this->polomer = r;
	}

	double Obsah()
	{
		double vysledek;
		vysledek = M_PI * polomer * polomer;
		return vysledek;
	}
	double Obvod()
	{
		double vysledek;
		vysledek = 2 * M_PI * polomer;
		return vysledek;
	}
};

class Obdelnik : public Tvar
{
protected:
	double sirka;
	double vyska;

public:
	Obdelnik(double a, double b)
	{
		this->sirka = a;
		this->vyska = b;
	}

	double Obsah()
	{
		double vysledek;
		vysledek = sirka * vyska;
		return vysledek;
	}
	double Obvod()
	{
		double vysledek;
		vysledek = 2 * (sirka + vyska);
		return vysledek;
	}

};

class Ctverec : public Obdelnik
{

public:
	Ctverec(double a) : Obdelnik(a, a) {}

};




int main()
{
	Kruh kruh(5.0);
	Obdelnik obdelnik(4.0, 6.0);
	Ctverec ctverec(5.0);

	cout << "Kruh:" << endl;
	cout << "Obsah: " << kruh.Obsah() << endl;
	cout << "Obvod: " << kruh.Obvod() << endl << endl;

	cout << "Obdelnik:" << endl;
	cout << "Obsah: " << obdelnik.Obsah() << endl;
	cout << "Obvod: " << obdelnik.Obvod() << endl << endl;

	cout << "Ctverec:" << endl;
	cout << "Obsah: " << ctverec.Obsah() << endl;
	cout << "Obvod: " << ctverec.Obvod() << endl << endl;


	return 0;
}