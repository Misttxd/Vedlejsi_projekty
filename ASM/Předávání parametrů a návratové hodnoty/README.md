# Předávání parametrů a návratové hodnoty

Tento projekt demonstruje, jak předávat parametry (pole a řetězce) z jazyka C do funkcí v assembleru (NASM) a jak vracet hodnoty zpět.

## Obsah

- **`c-main.c`**: Hlavní C kód, který volá funkce z assembleru s různými parametry a tiskne výsledky.
- **`asm-module.asm`**: Modul v assembleru, který obsahuje dvě hlavní funkce:
    - `str_merge_diff`: Funkce pro práci s řetězci, která má za úkol vytvořit nový řetězec obsahující znaky, které jsou v prvním řetězci, ale ne ve druhém. V kódu je i druhá, opravená verze `str_merge_diff2`.
    - `arr_sum_and_return`: Sečte prvky na zadaném indexu ze dvou polí a vrátí výsledek. Obsahuje kontrolu proti čtení mimo hranice pole.

## Cíl

Cílem projektu je ukázat konvence volání pro předávání složitějších datových typů jako jsou pole a řetězce do assembleru a jak bezpečně pracovat s ukazateli a vracet hodnoty.
