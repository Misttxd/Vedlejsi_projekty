# Projekt: Interaktivní CSV editor

Tento projekt je nástroj příkazové řádky napsaný v jazyce C pro interaktivní manipulaci s daty v souborech formátu CSV. Program načte data do paměti a umožňuje uživateli provádět databázové operace, jako je přidávání řádků/sloupců a výpočet statistik.

## Funkcionalita

-   Načítání a parsování CSV souborů.
-   Dynamická alokace paměti pro uložení datové struktury.
-   Interaktivní konzole pro zadávání příkazů.
-   Provádění operací pro úpravu dat (přidání řádku/sloupce).
-   Výpočet agregovaných dat (průměr, min, max, suma).
-   Uložení upravených dat do výstupního souboru.

## Použití
Tento projekt slouží jako praktické cvičení pro manipulaci s CSV soubory, správu dynamické paměti a návrh uživatelského rozhraní příkazové řádky (CLI) v C.


## Příkazy

-   `addrow <hodnota1>,<hodnota2>,...`: Přidá nový řádek. Počet hodnot musí odpovídat počtu sloupců.
-   `addcol <název> <hodnota1>,<hodnota2>,...`: Přidá nový sloupec. Počet hodnot musí odpovídat počtu řádků.
-   `average <název-sloupce>`: Vypíše průměrnou hodnotu zadaného sloupce.
-   `max <název-sloupce>`: Vypíše největší hodnotu zadaného sloupce.
-   `min <název-sloupce>`: Vypíše nejmenší hodnotu zadaného sloupce.
-   `sum <název-sloupce>`: Vypíše součet hodnot zadaného sloupce.
-   `print`: Vypíše aktuální stav databáze do konzole.
-   `exit`: Uloží změny do výstupního souboru a ukončí program.
