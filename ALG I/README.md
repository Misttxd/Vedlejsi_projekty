# Projekt: Slévání Seřazených Seznamů

Tento projekt v C++ implementuje algoritmus pro slévání `k` seřazených seznamů čísel do jednoho výsledného seřazeného seznamu. Program je navržen pro efektivní zpracování velkého počtu vstupních souborů.

## Popis Algoritmu

Program využívá metodu opakovaného hledání minima. V každém kroku iteruje přes všechny vstupní seznamy, najde nejmenší aktuálně dostupný prvek a zapíše ho do výstupního souboru. Tento proces se opakuje, dokud nejsou všechny vstupní seznamy zcela zpracovány.

## Struktura Projektu

-   `Projekt_main.cpp`: Hlavní soubor, který zpracovává argumenty příkazové řádky, načítá data ze souborů a řídí celý proces.
-   `Slevani.cpp`: Obsahuje logiku samotného slévacího algoritmu.
-   `Seznam.h`: Definuje jednoduchou třídu `Seznam` pro uchování dat a aktuálního stavu každého vstupního seznamu.
-   `generate_data.py`: Python skript pro vygenerování sady testovacích dat.

## Použití
Tento projekt slouží jako akademické cvičení pro pochopení a implementaci algoritmů slévání seřazených seznamů. Obsahuje pomocný Python skript pro generování dat a C++ zdrojové soubory.

