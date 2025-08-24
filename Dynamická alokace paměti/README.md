# Projekt 5: Želví grafika

Program v C, který simuluje "želví grafiku" na textové mřížce. Umožňuje ovládat jednu nebo více želv pomocí jednoduchých příkazů a kreslit tak obrazce. Program demonstruje dynamickou alokaci 2D pole.

## Použití

Program nejprve očekává rozměry mřížky (`šířka` a `výška`). Poté přijímá sekvenci jednopísmenných příkazů.

### Příkazy
-   `r`: Otočí všechny želvy doprava.
-   `l`: Otočí všechny želvy doleva.
-   `m`: Posune všechny želvy o krok vpřed.
-   `o`: Přepne stav buňky pod každou želvou (kreslí/maže 'o').
-   `f`: Přidá novou želvu (max. 3).
-   `x`: Vykreslí finální stav mřížky a ukončí program.

### Kompilace
```sh
gcc Projekt5_BRU0098.c -o zelva
```

### Spuštění
Vstupní data (rozměry a příkazy) je možné předat programu pomocí `echo`:
```sh
echo "10 10 omfomox" | ./zelva
```

### Příklad výstupu
Pro vstup `10 10 omfomox` program vykreslí následující mřížku:
```
..o.......
..........
..........
..........
..........
..........
..........
..........
..........
..........
```
