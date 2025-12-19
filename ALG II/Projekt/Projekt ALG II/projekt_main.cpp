/**
 * @file projekt_main.cpp
 * @brief Hlavní soubor pro spuštění analýzy závislostí.
 * 
 * Tento soubor obsahuje hlavní funkci, která
 * zpracuje argumenty z příkazové řádky, vytvoří instanci třídy pro analýzu,
 * spustí výpočet a vytiskne výsledky na standardní výstup.
 */
#include "zavislosti.h"
#include <iostream>
#include <vector>
#include <algorithm>
#include <string>

using std::cout, std::endl, std::vector, std::sort, std::string;

/**
 * @brief Vstupní bod programu.
 * Jako vstupní hodnota programu je očekáván textový soubor ve kterém jsou uloženy hodnoty reprezentující závislosti mezi soubory oddělené "->"
 * @param argc Počet argumentů příkazové řádky.
 * @param argv Pole argumentů příkazové řádky.
 * @return int Návratový kód (0 pro úspěch, 1 pro chybu).
 */
int main([[maybe_unused]] int argc, char *argv[])
{
    string filename = argv[1];

    ZavislostiZdrojovychKodu zavislostiZdrojovychKodu;

    zavislostiZdrojovychKodu.readDataFromFile(filename);

    Vector2D dependencies = zavislostiZdrojovychKodu.calculateDependencies();

    zavislostiZdrojovychKodu.printVector(dependencies);

    

    return 0;
}
