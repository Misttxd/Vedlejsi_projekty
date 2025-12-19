/**
 * @file zavislosti.h
 * @brief Hlavičkový soubor pro třídu ZavislostiZdrojovychKodu.
 * 
 * Definuje rozhraní třídy, která se stará o analýzu závislostí zdrojových kódů.
 * Obsahuje deklarace všech metod a členských proměnných.
 */
#pragma once

#include <vector>
#include <string>

using Vector2D = std::vector<std::vector<int>>;

/**
 * @class ZavislostiZdrojovychKodu
 * @brief Třída pro reprezentaci závislostí zdrojových kódů.
 *
 * Načítá graf závislostí ze souboru a umožňuje vypočítat, které soubory
 * je nutné překompilovat při změně jednoho konkrétního souboru.
 */
class ZavislostiZdrojovychKodu
{
private:
    /**
     * @brief Seznam sousednosti reprezentující graf závislostí.
     *
     * Každý vnitřní vektor na indexu 'i' obsahuje seznam "Zdrojových kódu" ve formě čísel, na kterých
     * "Zdrojový kód" 'i' přímo závisí. (u -> v, "u závisí na v")
     */
    Vector2D dependencyVector;
    /**
     * @brief Obrácený graf (v -> u, "v je vyžadován souborem u")
     */
    Vector2D reverseDependencyVector;
public:
    /**
     * @brief Vytiskne výsledný 2D vektor závislostí na standardní výstup.
     * @param printedVector 2D vektor, který se má vytisknout. Formát je "hlavniBod -> zavislost1, zavislost2, ...".
     */
    void printVector(const Vector2D &printedVector);

    /**
     * @brief Načte graf závislostí ze souboru a naplní vektor jeho daty.
     * @param filename Cesta k souboru se seznamem hran grafu (formát "u -> v").
     * @brief 
     * @brief Pro načítání ze souboru jsem se inspiroval zde: https://github.com/Korf-tms/Algoritmy-II/blob/main/fun/read_from_file.cpp
     * @brief Pro použití stringstream jsem se inspiroval zde: https://www.geeksforgeeks.org/cpp/stringstream-c-applications/
     */
    void readDataFromFile(const std::string &filename);

    /**
     * @brief Provede rekurzivní prohledávání do hloubky pro nalezení všech závislostí.
     * @param hledanaHodnota Hodnota, která se hledá v jednotlivých polích vstupního 2D vektoru.
     * @param finalDependencies Výsledný 2D vektor, do kterého se ukládají nalezené závislosti.
     * @param rootIndex Index původního souboru, jehož hodnota se mění podle procházeného pole v metodě "calculateDependencies".
     * @param visited Pomocný vektor pro sledování již navštívených uzlů k zamezení cyklů.
     * @param isCalculated Pomocný vektor pro sledování již vypčítaných závislostí souborů.
     */
    void RecursiveDFS(int hledanaHodnota, Vector2D &finalDependencies, int rootIndex, std::vector<bool> &visited, const std::vector<bool>& isCalculated);

    /**
     * @brief Spustí výpočet závislostí pro všechny soubory v grafu.
     * @return Vrací 2D vektor, kde každý řádek 'i' obsahuje seznam souborů,
     *         které je nutné překompilovat při změně souboru 'i'.
     */
    Vector2D calculateDependencies();
};
