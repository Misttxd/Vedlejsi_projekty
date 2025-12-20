/**
 * @file Projekt_main.cpp
 * @brief Hlavní soubor programu, který načítá vstupní data a spouští proces slévání.
 */

#include <iostream>
#include <fstream>
#include <vector>
#include <string>

#include "Seznam.h"
#include "Slevani.cpp"

using namespace std;


/**
 * @brief Main funkce programu.
 * 
 * Jsou očekávány 3 hodnoty:
 * 1. Počet vstupních souborů = k
 * 2. Adresář s testovacími soubory = adresar
 * 3. Jméno výstupního souboru = vystupniSoubor
 *
 * @param argc Počet argumentů.
 * @param argv Pole argumentů příkazové řádky.
 * @return int Návratový kód programu.
 */

int main(int argc, char const *argv[]) 
{
    const int k = stoi(argv[1]);
    string adresar = argv[2];
    string vystupniSoubor = argv[3];

    vector<Seznam> seznamy(k);

    for (int i = 0; i < k; ++i) 
    {
        string jmenoSouboru = adresar + "/" + to_string(i) + ".txt";
        ifstream vstup(jmenoSouboru);
        string cisloSTR;

        while (getline(vstup, cisloSTR)) 
        {
            seznamy[i].prvky.push_back(stoi(cisloSTR));
            //cout << cisloSTR << "\n";
        }

        vstup.close();
    }

    ofstream vystup(vystupniSoubor);

    Slevani(seznamy, vystup);

    vystup.close();
    return 0;
}
