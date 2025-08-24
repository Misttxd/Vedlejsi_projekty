# Práce s proměnnými v assembleru

Tento projekt demonstruje základní práci s proměnnými v assembleru (NASM) ve spojení s jazykem C.

## Obsah

- **`c-main.c`**: Hlavní C kód, který volá funkce z assembleru a tiskne výsledky.
- **`asm-module.asm`**: Modul v assembleru, který obsahuje dvě funkce:
    - `merge_and_extend`: Spojí dva `char` do jednoho `short` a rozšíří ho na `int`.
    - `decode`: Dekóduje 4-bajtový `int` na textový řetězec.

## Cíl

Cílem projektu je ukázat, jak sdílet a manipulovat s proměnnými mezi C a assemblerem.
