# Projekt 1: Bankomat

Jednoduchý program v jazyce C, který simuluje funkci bankomatu. Pro zadanou celkovou částku vypočítá a vypíše nejmenší možný počet bankovek potřebných k jejímu vyplacení.

## Použití

### Kompilace
Pro zkompilování programu použijte překladač GCC:
```sh
gcc Projekt1_BRU0098_2.c -o projekt1
```

### Spuštění
Program spustíte z příkazové řádky:
```sh
./projekt1
```

### Příklad výstupu
Program má napevno nastavenou částku `9420`. Výstup pro tuto hodnotu je:
```
Bankovka 5000: 1x
Bankovka 2000: 2x
Bankovka 1000: 0x
Bankovka 500: 0x
Bankovka 200: 2x
Bankovka 100: 0x
```
