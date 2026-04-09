# Zadání: Dynamické tabulky a triggery

## Cíl
Natrénovat dynamické SQL a trigger, který hlídá konzistenci odvozeného počtu autorů.

## Úkoly
1. Vytvoř proceduru, která pro každé `SID` v `z_field_of_science`:
   - vytvoří tabulku `test_<sid>`,
   - naplní ji agregovanými daty podle `FID`, `YEAR`, `RANKING`,
   - při existenci tabulky ji nejprve odstraní (nebo vyhodí vlastní výjimku ve variantě B).
2. Připrav pracovní kopie tabulek pro testování triggeru.
3. Vytvoř trigger (nebo sadu triggerů), který po INSERT/UPDATE/DELETE:
   - přepočítá `author_count` pro dotčený článek,
   - zachová konzistenci i při změně `aid` při UPDATE.

## Co si pohlídat
- Bezpečné skládání dynamických SQL příkazů.
- Rozlišení INSERT/DELETE/UPDATE větví v triggeru.
- Konečný stav počitadel po sérii operací.
