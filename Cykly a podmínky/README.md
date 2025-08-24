# Projekt 2: Kreslení obrazců

Tento program v jazyce C interaktivně kreslí různé geometrické obrazce a tvary do konzole na základě vstupu uživatele.

## Použití

Program očekává na vstupu tři celá čísla oddělená mezerou:
1.  `číslo obrazce` (0-7, 9) - určuje, jaký tvar se má kreslit.
2.  `šířka` (a) - horizontální rozměr obrazce.
3.  `výška` (b) - vertikální rozměr obrazce.

### Kompilace
```sh
gcc Projekt2_BRU0098.c -o projekt2
```

### Spuštění
Programu musíte předat vstupní data, například pomocí `echo`:
```sh
echo "1 10 5" | ./projekt2
```
Tento příklad zavolá obrazec č. 1 (dutý obdélník) o velikosti 10x5.

### Příklad výstupu
Pro vstup `1 10 5`:
```
xxxxxxxxxx
x        x
x        x
x        x
xxxxxxxxxx
```
