# Zadání: Kontrola konzistence a reporting

## Cíl
Natrénovat kontrolu datové konzistence a výpis reportu přes PL/SQL proceduru.

## Úkoly
1. Rozšiř tabulku článků o kopie statistik (`article_author_count`, `article_institution_count`).
2. Implementuj funkci `StatisticsConsistent(p_processType)` s režimy:
   - `Analyze` - pouze zjištění, zda jsou data konzistentní,
   - `Fix` - automatická oprava nesouladů.
3. Ošetři neplatný vstupní parametr (mimo `Analyze`/`Fix`).
4. Implementuj proceduru `ReportFord4Institution(p_institution_name)`:
   - ověří existenci a jednoznačnost instituce,
   - vypíše seznam Ford oborů navázaných na články instituce,
   - přidá číslovaný výpis výsledků.

## Co si pohlídat
- Jasné větvení logiky `Analyze` vs `Fix`.
- Korektní joiny přes články, časopisy a obory.
- Přehledný report i při prázdném výsledku.
