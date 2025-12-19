/**
 * @file zavislosti.cpp
 * @brief Implementační soubor pro třídu ZavislostiZdrojovychKodu.
 * 
 * Obsahuje definice metod deklarovaných v souboru zavislosti.h.
 */
#include "zavislosti.h"
#include <iostream>
#include <vector>
#include <algorithm>
#include <fstream>
#include <sstream>
#include <string>

using std::cout, std::endl, std::vector, std::sort;

/**
 * @brief Vytiskne 2D vektor ve specifickém formátu.
 * 
 * Metoda prochází vnější vektor a pro každý "Soubor"  vytiskne jeho číslo následované šipkou "->". 
 * Poté vypíše všechny prvky vnitřního vektoru (závislosti) oddělené čárkou. Řádky, které jsou prázdné, jsou přeskočeny a nevypisují se.
 */

void ZavislostiZdrojovychKodu::printVector(const Vector2D &printedVector)
{
    for (size_t i = 0; i < printedVector.size(); i++)
    {
        if (printedVector[i].empty())
        {
            continue;
        }

        cout << i << " -> ";
        for (size_t y = 0; y < printedVector[i].size(); y++)
        {
            cout << printedVector[i][y];
            if (y + 1 < printedVector[i].size())
            {
                cout << ", ";
            }
        }
        cout << endl;
    }
}

/**
 * @brief Načítá data o závislostech ze souboru.
 * 
 * Metoda prochází soubor řádek po řádku. Každý řádek, který reprezentuje
 * jednu nebo více závislostí (např. "0 -> 1 9"), zpracuje pomocí
 * stringstreamu. Postupně z řádku vytáhne jednotlivá čísla a uloží je
 * do vnitřní datové struktury (seznamu sousednosti), kterou průběžně
 * zvětšuje podle nejvyššího nalezeného čísla souboru.
 * Datová struktura zároveň obsahuje pro každý index vnějšího pole jeho hodnotu ve vnitřím poli na první pozici ([i][0]),
 * to ulehčuje práci ve vyhledávání, takto to funguje i pro soubory, které nemají žádné závislosti, ty se naplní vlastní hodnoutou.
 * Díky tomu nedojde nikdy že nějaké pole je prázdné a toto riziko se nemusí nikde ošetřovat.
 * 
 * Během načítání také plní 'reverseDependencyVector' obrácenými hranami (v -> u),
 * který slouží pro následnou optimalizaci výpočtu.
 * 
 * @brief Pro načítání ze souboru jsem se inspiroval zde: https://github.com/Korf-tms/Algoritmy-II/blob/main/fun/read_from_file.cpp
 * @brief Pro použití stringstream jsem se inspiroval zde: https://www.geeksforgeeks.org/cpp/stringstream-c-applications/
 */
void ZavislostiZdrojovychKodu::readDataFromFile(const std::string &filename)
{
    std::ifstream file(filename);
    Vector2D directDependencies;
    Vector2D reverseDependencies;

    if (!file.is_open())
    {
        std::cerr << "Unable to open file: " << filename << endl;
        return;
    }

    std::string line;
    int maxNum = -1;

    while (std::getline(file, line))
    {
        std::stringstream ss(line);
        int mainNum; // u
        [[maybe_unused]] std::string arrow;
        
        if (!(ss >> mainNum)) 
        {
            continue; 
        }
        
        if (mainNum > maxNum) 
        {
            maxNum = mainNum;
        }


        if (static_cast<size_t>(maxNum) >= directDependencies.size())
        {
            directDependencies.resize(maxNum + 1);
            reverseDependencies.resize(maxNum + 1);
        }

        if (directDependencies[mainNum].empty())
        {
            directDependencies[mainNum].push_back(mainNum);
        }

        ss >> arrow;

        int dependentNode; // v
        while (ss >> dependentNode)
        {
            directDependencies[mainNum].push_back(dependentNode);

            if (dependentNode > maxNum)
            {
                maxNum = dependentNode;
            }
            if (static_cast<size_t>(maxNum) >= directDependencies.size())
            {
                directDependencies.resize(maxNum + 1);
                reverseDependencies.resize(maxNum + 1);
            }

            reverseDependencies[dependentNode].push_back(mainNum);
        }
    }

    //directDependencies.resize(maxNum + 1);
    //reverseDependencies.resize(maxNum + 1);

    for (size_t i = 0; i < directDependencies.size(); i++)
    {
        if (directDependencies[i].empty())
        {
            directDependencies[i].push_back(i);
        }
    }

    file.close();
    this->dependencyVector = directDependencies;
    this->reverseDependencyVector = reverseDependencies;
    printVector(dependencyVector);
    cout << endl;
    printVector(reverseDependencies);
    cout << endl;
}

 /**
 * @brief Hledá soubory závislé na daném souboru pomocí rekurze.
 * 
 * Tato funkce je jádrem celého algoritmu. Funguje jako prohledávání
 * do hloubky (DFS) na OBRÁCENÉM grafu závislostí.
 * 
 * Okamžitě se podívá do 'reverseDependencyVector' a zjistí, které soubory 'node'
 * přímo závisí na 'hledanaHodnota'.
 * Pro každý takový nalezený soubor 'node' se rekurzivně zanoří a hledá,
 * kdo pro změnu závisí na 'node'. Tím odhalí celý řetězec závislostí.
 * 
 * Dále je zde aplikována optimalizace (isCalculated), která se stará o to, že když jsou už nalezeny závislosti nějakého souboru, nemusí se hledat znova, ale rovnou se přidají z předešlých průchodů.
 */
void ZavislostiZdrojovychKodu::RecursiveDFS(int hledanaHodnota, Vector2D &finalDependencies, int rootIndex, vector<bool> &visited, const vector<bool>& isCalculated)
{
       for (int node : reverseDependencyVector[hledanaHodnota])
       {
           if (visited[node] == false)
           {

                visited[node] = true;
                finalDependencies[rootIndex].push_back(node);

                if (isCalculated[node]) 
                {
                    for (int dependency : finalDependencies[node]) 
                    {
                        if (visited[dependency] == false) 
                        {
                            visited[dependency] = true;
                            finalDependencies[rootIndex].push_back(dependency);        
                        }
                    }
                } 
                else 
                {
                    RecursiveDFS(node, finalDependencies, rootIndex, visited, isCalculated);
                }
            }
        }
           
       
   }

/**
 * @brief Pro každý soubor v projektu vypočítá seznam souborů, které je nutné překompilovat.
 * 
 * Tato metoda funguje jako hlavní smyčka. Postupně prochází všechny soubory
 * v projektu (od 0 do N). Pro každý soubor 'i' spustí rekurzivní hledání 'RecursiveDFS'.
 * Využívá se ze toho, že hlednaná hodnota je vždy zároveň první prvek jednotlivých vnějších polí, tak se to zachová i pro výstupní pole.
 * 
 * Výsledkem je 2D vektor, kde každý řádek 'i'
 * obsahuje kompletní seznam souborů, které je nutné překompilovat,
 * pokud dojde ke změně v souboru 'i'.
 */
Vector2D ZavislostiZdrojovychKodu::calculateDependencies()
{ 
    Vector2D finalDependencies(dependencyVector.size());
    vector<bool> isCalculated(dependencyVector.size(), false);

    for (size_t i = 0; i < dependencyVector.size(); i++)
    {
        int hledanaHodnota = dependencyVector[i][0];
        finalDependencies[i].push_back(hledanaHodnota);

        vector<bool> visited(dependencyVector.size(), false);
        visited[hledanaHodnota] = true;

        RecursiveDFS(hledanaHodnota, finalDependencies, i, visited, isCalculated);
        
        isCalculated[i] = true;
        sort(finalDependencies[i].begin(), finalDependencies[i].end());
    }

    return finalDependencies;
}
