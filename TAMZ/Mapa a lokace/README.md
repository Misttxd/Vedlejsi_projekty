# Mapa a lokace

Ionic React aplikace pro vyhledávání míst přes OpenStreetMap Nominatim a jejich zobrazení na mapě pomocí Leafletu.

## Popis funkčnosti
- Vyhledává místo podle názvu zadaného uživatelem.
- Ukládá nalezená místa do seznamu se souřadnicemi.
- Přepíná mezi formulářem se seznamem míst a mapovým pohledem.
- Zobrazuje uložená místa jako markery na mapě.
- Obsahuje základní unit test a Cypress e2e test.

## Spuštění

```bash
npm install
npm run dev
```

## Ověření

```bash
npm run build
npm run test.unit -- --run
```
