/**
 * @file Seznam.h
 * @brief Hlavičkový soubor obsahující definici třídy pro ukládání obsahu jednotlivých vstupních souborů a proměnnou pro jeho indexování.
 */

#include <vector>

#pragma once

using namespace std;

/**
 * @class Seznam
 * @brief Uloží obsah vybraného souboru a aktuální index (začíná na 0).
 */

class Seznam 
{
public:
    vector<int> prvky;
    int index = 0;
};