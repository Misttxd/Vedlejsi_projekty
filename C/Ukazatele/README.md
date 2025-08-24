# Projekt 4: Generátor histogramu

Tento program v jazyce C generuje a vypisuje textový histogram na základě zadaných dat. Program je schopen generovat horizontální i vertikální histogram.

## Použití

Program je interaktivní a očekává sérii vstupů:
1.  **Orientace:** Znak `h` pro horizontální nebo `v` pro vertikální.
2.  **Parametry:** Dvě celá čísla `n` a `m`, kde `n` je počet hodnot, které se budou zpracovávat, a `m` je počáteční hodnota prvního sloupce histogramu.
3.  **Data:** `n` celočíselných hodnot, každá na novém řádku nebo oddělená mezerou.

### Kompilace
```sh
gcc cv4.c -o histogram
```

### Spuštění
Vstupní data je nejlepší předat programu pomocí přesměrování ze souboru nebo pomocí `echo` s formátováním pro nové řádky.

**Příklad pro horizontální histogram:**
```sh
# Vytvoření souboru s daty
echo -e "h\n5 10\n10\n11\n10\n12\n20" > data.txt

# Spuštění s přesměrováním
./histogram < data.txt
```

### Příklad výstupu
Pro výše uvedená data program vygeneruje následující horizontální histogram:
```
10 ##
11 #
12 #
13
14
15
16
17
18
invalid: #
```
