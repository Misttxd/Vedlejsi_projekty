# Projekt 6: Transformace textu

Tento program v jazyce C provádí komplexní úpravy a formátování textových řetězců. Program načte počet řádků ke zpracování a poté pro každý řádek provede sérii transformací.

## Pravidla transformace

Pro každý vstupní řetězec program provede následující kroky:
1.  **Oříznutí:** Odstraní všechny bílé znaky (mezery) na začátku a na konci řetězce.
2.  **Zpracování slov:** Rozdělí řetězec na jednotlivá slova a každé slovo upraví:
    *   Pokud slovo obsahuje alespoň jedno velké písmeno, změní se na formát "PrvníVelké" (první písmeno velké, zbytek malá).
    *   Pokud slovo obsahuje pouze malá písmena, změní se celé na velká písmena.
3.  **Odstranění duplicit:** V každém upraveném slově odstraní po sobě jdoucí duplicitní znaky (např. `HELO` z `HHEEELLOO`).
4.  **Spojení:** Spojí upravená slova zpět do jednoho řetězce odděleného jednou mezerou.
5.  **Statistika:** Vypíše statistiku porovnávající počet malých písmen, velkých písmen a mezer v původním a upraveném řetězci.

## Použití

### Kompilace
```sh
gcc Projekt6_BRU0098.c -o transform
```

### Spuštění
Program nejprve očekává číslo `n`, které udává, kolik řádků bude následovat. Vstup je možné předat pomocí souboru.

**Příklad:**
Obsah souboru `data.txt`:
```
2
   hheeelloo   wOOOrld   
  testing 123  
```

Spuštění:
```sh
./transform < data.txt
```

### Příklad výstupu
```
HELO World
lowercase: 13 -> 4
uppercase: 3 -> 5
spaces: 9 -> 1

TESTING 123
lowercase: 7 -> 0
uppercase: 0 -> 7
spaces: 5 -> 1
```
