# Zadání: Vkládání článku a JSON export

## Cíl
Procvičit robustní validační logiku v proceduře a export dat do JSON formátu.

## Úkoly
1. Vytvoř proceduru `P_InsertArticle(...)`, která:
   - ověří existenci časopisu podle ISSN,
   - ověří existenci instituce podle názvu,
   - ověří existenci autora podle jména,
   - vloží nový článek,
   - vloží vazby do spojovacích tabulek,
   - správně nastaví počitadla autorů a institucí.
2. V případě nejednoznačných nebo neplatných vstupů vrať smysluplnou chybu.
3. Vytvoř funkci `F_ExportArticleJSON(p_aid)`, která vrátí JSON článku.
4. Doplň i správu rankingů pro kombinaci obor–časopis–rok.
5. Vytvoř export časopisu za rok do JSON včetně oborů a článků.

## Co si pohlídat
- Transakční konzistence při více vkládaných krocích.
- Korektní práce s textem při ručním skládání JSON.
- Jednoznačné chování při NO_DATA_FOUND a TOO_MANY_ROWS.
