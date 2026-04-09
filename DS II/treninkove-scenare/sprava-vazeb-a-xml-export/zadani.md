# Zadání: Správa vazeb a XML export

## Cíl
Natrénovat bezpečnou práci s vazebními tabulkami článků, autorů a institucí a zároveň výstup dat do XML.

## Úkoly
1. Vytvoř funkci `F_SetArticleInstitution(p_aid, p_iid, p_set)`.
2. Funkce má umět vložit nebo odebrat vazbu článek–instituce podle hodnoty `p_set`.
3. Při změně vazby aktualizuj počitadlo `institution_count` v tabulce článků.
4. Vracej stavovou hodnotu:
   - `I` při vložení,
   - `D` při smazání,
   - `N` pokud se nic nezměnilo.
5. Doplň export článku do XML včetně:
   - základních údajů o článku,
   - seznamu institucí,
   - seznamu autorů.
6. Přidej analogickou funkci pro vazbu článek–autor a aktualizaci `author_count`.

## Co si pohlídat
- Ošetření duplicitních vazeb.
- Ošetření mazání neexistující vazby.
- Konzistence počitadel po každé operaci.
