# Zadání: Vzorkování a výpočet odměn

## Cíl
Procvičit kombinaci dynamického SQL, validace parametrů a doménové logiky odměn.

## Úkoly
1. Implementuj proceduru `PSampleIntColumn(p_table, p_column, p_sample_rate)`.
2. Zkontroluj rozsah `p_sample_rate` (0 až 100), při neplatné hodnotě ošetři chybu.
3. Vytvoř tabulku `<p_table>_SAMPLE` a naplň ji vzorkem dat ze zadaného sloupce.
4. Implementuj funkci `FGetArticleReward(p_aid)`:
   - základ odměny podle rankingu (`Decil`, `Q1`, `Q2`, `Q3`, `Q4`),
   - bonus za instituci z Ostravy,
   - penalizace za instituci z Brna,
   - návrat minimálně 0.
5. Volitelně připrav druhou variantu vzorkování pro textový sloupec.

## Co si pohlídat
- Ochrana proti neplatným parametrům.
- Jasné oddělení výpočtu základu a bonusů.
- Smysluplné výstupní chování při chybách.
