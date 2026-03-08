# Memory App

Aplikace pro ukládání jednoduchých záznamů (úkolů), napsaná v HTML/JS s využitím frameworku **Ionic** a nástroje **Vite**.

## Popis funkčnosti
- Umožňuje přidávat záznamy s vlastním názvem a nastavenou prioritou (nízká, střední, vysoká).
- Vizuálně odlišuje priority pomocí barevných ikon (např. červená ikona pro vysokou prioritu, zelená pro nízkou).
- Ukládá vytvořené záznamy lokálně v prohlížeči pomocí `localStorage`, takže zůstanou dostupné i po obnovení stránky.
- Umožňuje smazat jednotlivé záznamy nebo odstranit všechny položky najednou.
- V hlavičce zobrazuje celkový počet aktuálních záznamů.

## Spuštění projektu
1. Nainstalujte závislosti:
   ```bash
   npm install
   ```
2. Spusťte webový vývojový server:
   ```bash
   npm run dev
   ```
