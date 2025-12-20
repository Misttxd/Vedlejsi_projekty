# Projekt: Slévání Seřazených Seznamů

Tento projekt v C++ implementuje algoritmus pro slévání `k` seřazených seznamů čísel do jednoho výsledného seřazeného seznamu. Program je navržen pro efektivní zpracování velkého počtu vstupních souborů.

## Popis Algoritmu

Program využívá metodu opakovaného hledání minima. V každém kroku iteruje přes všechny vstupní seznamy, najde nejmenší aktuálně dostupný prvek a zapíše ho do výstupního souboru. Tento proces se opakuje, dokud nejsou všechny vstupní seznamy zcela zpracovány.

## Struktura Projektu

-   `Projekt_main.cpp`: Hlavní soubor, který zpracovává argumenty příkazové řádky, načítá data ze souborů a řídí celý proces.
-   `Slevani.cpp`: Obsahuje logiku samotného slévacího algoritmu.
-   `Seznam.h`: Definuje jednoduchou třídu `Seznam` pro uchování dat a aktuálního stavu každého vstupního seznamu.
-   `generate_data.py`: Python skript pro vygenerování sady testovacích dat.

## Jak program použít

### 1. Příprava dat

Program jako vstup očekává adresář se soubory `0.txt`, `1.txt`, ..., `k-1.txt`, kde každý soubor obsahuje seřazená celá čísla (jedno číslo na řádek).

Pro snadné vytvoření testovacích dat můžete použít přiložený Python skript:
```sh
python generate_data.py
```
Tento příkaz vytvoří podadresář `vstupni_data` s 10 vzorovými soubory.

### 2. Kompilace

Program zkompilujte pomocí C++ kompilátoru (např. g++):
```sh
g++ Projekt_main.cpp -o slevani
```

### 3. Spuštění

Program se spouští z příkazové řádky se třemi argumenty:
1.  Počet vstupních souborů (`k`).
2.  Cesta k adresáři s daty.
3.  Název výstupního souboru.

Příklad spuštění (po vygenerování dat):
```sh
./slevani 10 ./vstupni_data merged_output.txt
```
Tento příkaz zpracuje 10 souborů z adresáře `vstupni_data` a výsledek uloží do `merged_output.txt`.
