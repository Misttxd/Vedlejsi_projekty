/**
 * @file Slevani.cpp
 * @brief Dodatkový soubor obsahující funkci pro slévání a třídění vstupních dat.
 */
#include <fstream>
#include <vector>

#include "Seznam.h"

using namespace std;



/**
 * @brief Sloučí všechny seznamy dohromady do jednoho výstupního souboru.
 * @param seznamy Vektor seznamů vybraných uživatelem.
 * @param vystup Výstupní soubor, do kterého bude zapsán výsledek.
 */
void Slevani(vector<Seznam>seznamy, ofstream &vystup) 
{
    bool done = false;
    bool first_line = true;

    while (done == false) 
    {
        done = true;
        int SeznamMinIndex = -1;
        int minHodnota = 0;


        for (int i = 0; i < seznamy.size(); i++) 
        {
            if (seznamy[i].index < seznamy[i].prvky.size()) 
            {
                done = false;
                int hodnota = seznamy[i].prvky[seznamy[i].index];
                if (SeznamMinIndex == -1 || hodnota < minHodnota) 
                {
                    SeznamMinIndex = i;
                    minHodnota = hodnota;
                }
            }
        }


        if (done == false) 
        {
            if (first_line == true)
            {
                vystup << minHodnota;
                first_line = false;
            }

            else
            {
                vystup << endl << minHodnota;
            }
            seznamy[SeznamMinIndex].index++;
        }
    }
}