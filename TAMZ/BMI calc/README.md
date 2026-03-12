# BMI Calculator

Tato aplikace slouží jako jednoduchá BMI kalkulačka s možností ukládání historie výpočtů. Je postavena na webových technologiích s využitím [Ionic Frameworku](https://ionicframework.com/) pro UI komponenty a [Vite](https://vitejs.dev/) pro rychlý vývoj.

## Funkce
- **Výpočet BMI**: Umožňuje zadat jméno, věk, pohlaví, výšku a váhu.
- **Hodnocení**: Na základě vypočtené hodnoty aplikace zobrazí výsledek v přehledném modálním okně (např. nadváha, normální váha, obezita).
- **Historie záznamů**: Záznamy se ukládají pomocí `localStorage` a uživatel k nim má přístup v záložce History, odkud je lze i odstranit.

## Technologie
- **HTML/CSS/JavaScript**: Základní webové technologie.
- **Ionic Framework**: Využití webových komponent pro vzhled mobilní aplikace (použito přes CDN v `index.html`).
- **Vite**: Moderní nástroj pro sestavení projektu a lokální vývojový server.

## Spuštění projektu

1. Ujistěte se, že máte nainstalovaný [Node.js](https://nodejs.org/).
2. Nainstalujte závislosti:
   ```bash
   npm install
   ```
3. Spusťte vývojový server:
   ```bash
   npm run dev
   ```
4. Aplikace bude dostupná na lokální adrese zobrazené v terminálu (typicky `http://localhost:5173`).
