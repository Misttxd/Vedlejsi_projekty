# Projekt 7: Report z dat o akciích

Tento program v C slouží jako nástroj pro generování HTML reportů z finančních dat. Program přijímá data ve formátu CSV ze standardního vstupu a na základě argumentů příkazové řádky generuje report pro specifickou akciovou značku (ticker).

## Funkcionalita
-   Načítá data o akciích (den, ticker, otevírací cena, zavírací cena, objem) z CSV.
-   Používá dynamicky alokované struktury pro uložení dat.
-   Identifikuje den s nejvyšším objemem obchodů pro zadaný ticker.
-   Generuje HTML stránku, která obsahuje:
    -   Shrnutí pro den s nejvyšším objemem.
    -   Tabulku všech načtených dat, kde jsou řádky pro sledovaný ticker zvýrazněny.
-   Formátuje velká čísla objemů pomocí podtržítek pro lepší čitelnost.

## Použití

Program vyžaduje dva argumenty příkazové řádky a data na standardním vstupu.

### Argumenty
1.  `ticker`: Značka akcie, která se má analyzovat (např. `AMC`).
2.  `n`: Počet řádků (dní), které se mají načíst ze vstupu.

### Kompilace
```sh
gcc Projekt7_BRU0098.c -o report
```

### Spuštění
Vstupní CSV data je třeba přesměrovat do programu.

**Příklad:**
Obsah souboru `data.csv`:
```
1,GME,150.50,160.75,12000
2,AMC,30.25,35.50,85000
3,GME,162.00,158.25,11500
4,AMC,36.00,34.75,95000
```

Spuštění z příkazové řádky:
```sh
./report AMC 4 < data.csv > report.html
```
Tento příkaz analyzuje 4 řádky dat pro ticker `AMC` a uloží výstup do souboru `report.html`.

### Příklad výstupu (`report.html`)
Výstupem je kompletní HTML stránka obsahující shrnutí a tabulku s daty.
