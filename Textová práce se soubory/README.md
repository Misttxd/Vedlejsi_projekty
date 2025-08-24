# Projekt: Sisyphus - Vyhledávání v souborech

`sisyphus` je jednoduchý nástroj příkazové řádky napsaný v C, který funguje podobně jako `grep`. Umožňuje vyhledat zadaný textový řetězec (needle) v souboru (haystack) a vypsat odpovídající řádky.

## Funkcionalita
-   Vyhledávání textu v určeném souboru.
-   Možnost case-insensitive (nerozlišující velká a malá písmena) vyhledávání pomocí přepínače `-i`.
-   Možnost přesměrovat výstup do zadaného souboru pomocí přepínače `-o`.
-   Robustní parsování argumentů příkazové řádky pro správné zpracování vstupů.

## Použití

### Kompilace
```sh
gcc sisyphus.c -o sisyphus
```
**Poznámka:** Program využívá funkci `strcasestr`, která je rozšířením GNU. Kompilace na některých systémech (např. Windows s MinGW) může vyžadovat speciální flag `-D_GNU_SOURCE` nebo nemusí být úspěšná, pokud knihovna tuto funkci neposkytuje.

### Argumenty
Program se spouští s následujícími argumenty:
```
./sisyphus <vstupní_soubor> <hledaný_řetězec> [volitelné_přepínače]
```
-   `<vstupní_soubor>`: Cesta k souboru, ve kterém se má hledat.
-   `<hledaný_řetězec>`: Text, který se má vyhledat.
-   `[-i]`: Volitelný přepínač pro vyhledávání bez ohledu na velikost písmen.
-   `[-o <výstupní_soubor>]`: Volitelný přepínač pro uložení výsledků do souboru.

### Příklad
Mějme soubor `data.txt` s obsahem:
```
Hello World
hello world
Another line
HELLO
```

Spuštění příkazu pro case-insensitive vyhledávání slova "hello" s výstupem do `results.txt`:
```sh
./sisyphus data.txt hello -i -o results.txt
```

### Výstup (`results.txt`)
```
Hello World
hello world
HELLO
```
