# Práce s poli a řetězci v assembleru

Tento projekt demonstruje různé operace s poli a textovými řetězci v assembleru (NASM) ve spojení s jazykem C.

## Obsah

- **`c-main.c`**: Hlavní C kód, který volá funkce z assembleru a tiskne výsledky.
- **`asm-module.asm`**: Modul v assembleru, který obsahuje čtyři funkce:
    - `pocitadlo_lichych`: Spočítá počet lichých čísel v poli.
    - `pocitadlo_sudych`: Vypočítá průměr sudých čísel v poli.
    - `nahrazeni_znaku`: Nahradí všechny výskyty zadaného znaku v řetězci novým znakem.
    - `ROT_13`: Aplikuje šifru ROT13 na textový řetězec.

## Cíl

Cílem projektu je ukázat, jak provádět základní i pokročilejší operace s datovými strukturami jako jsou pole a řetězce v assembleru.
