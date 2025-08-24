# Projekt 3: Kreslení s `drawing.h`

Program v jazyce C, který využívá externí grafickou knihovnu `drawing.h` k vykreslování různých scén, včetně květin, louky a jednoduché animace.

## Funkcionalita

Program na základě číselného vstupu od uživatele (0-4) volá různé funkce pro kreslení:
-   Přerušovaná čára
-   Schody
-   Kompozice květin
-   Louka s květinami
-   Jednoduchá animace

## Použití

### Závislosti
Tento projekt vyžaduje ke kompilaci hlavičkový soubor `drawing.h` a jeho odpovídající implementaci (např. `drawing.c` nebo zkompilovanou knihovnu). Tyto soubory nejsou součástí tohoto repozitáře.

### Kompilace
Pokud máte k dispozici soubor `drawing.c`, můžete projekt zkompilovat následujícím příkazem:
```sh
gcc Projekt3_BRU0098.c drawing.c -o projekt3
```

### Spuštění
Program po spuštění očekává na vstupu jedno číslo (0-4), které určí, jaká scéna se má vykreslit.
```sh
echo "2" | ./projekt3
```
Tento příklad by zavolal funkci pro vykreslení kompozice květin.
