# Projekt: Interaktivní CSV editor

Tento projekt je nástroj příkazové řádky napsaný v jazyce C pro interaktivní manipulaci s daty v souborech formátu CSV. Program načte data do paměti a umožňuje uživateli provádět databázové operace, jako je přidávání řádků/sloupců a výpočet statistik.

## Funkcionalita

-   Načítání a parsování CSV souborů.
-   Dynamická alokace paměti pro uložení datové struktury.
-   Interaktivní konzole pro zadávání příkazů.
-   Provádění operací pro úpravu dat (přidání řádku/sloupce).
-   Výpočet agregovaných dat (průměr, min, max, suma).
-   Uložení upravených dat do výstupního souboru.

## Sestavení projektu (Build)

Projekt je určen k sestavení pomocí `CMake` a `make`. Pro sestavení postupujte následovně:

```sh
# Vytvoření a vstup do adresáře pro sestavení
mkdir build
cd build

# Spuštění CMake pro vygenerování Makefile
cmae ..

# Vlastní kompilace
make
```
Po úspěšném sestavení naleznete spustitelný soubor `PROJEKT` v adresáři `build`.

## Použití

Program se spouští z příkazové řádky s argumenty pro vstupní a výstupní soubor.

```sh
./PROJEKT --input input.csv --output output.csv
```
-   `--input <cesta>`: Vstupní CSV soubor, který se má načíst.
-   `--output <cesta>`: Soubor, do kterého se uloží data po ukončení programu příkazem `exit`.

Po spuštění program vypíše informace o načteném souboru a vstoupí do interaktivního režimu, kde očekává příkazy.

## Příkazy

-   `addrow <hodnota1>,<hodnota2>,...`: Přidá nový řádek. Počet hodnot musí odpovídat počtu sloupců.
-   `addcol <název> <hodnota1>,<hodnota2>,...`: Přidá nový sloupec. Počet hodnot musí odpovídat počtu řádků.
-   `average <název-sloupce>`: Vypíše průměrnou hodnotu zadaného sloupce.
-   `max <název-sloupce>`: Vypíše největší hodnotu zadaného sloupce.
-   `min <název-sloupce>`: Vypíše nejmenší hodnotu zadaného sloupce.
-   `sum <název-sloupce>`: Vypíše součet hodnot zadaného sloupce.
-   `print`: Vypíše aktuální stav databáze do konzole.
-   `exit`: Uloží změny do výstupního souboru a ukončí program.
