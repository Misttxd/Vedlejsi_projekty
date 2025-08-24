# Projekt FPR: Tisk cesty ke komponentě v GUI

Tento projekt obsahuje Haskell funkci, která najde a vytiskne hierarchickou cestu ke komponentě se zadaným názvem v GUI struktuře.

## Funkcionalita

Funkce `vytiskniCestu` rekurzivně prochází stromovou strukturu GUI prvků a pokud najde komponentu s cílovým názvem, vrátí řetězec představující cestu k této komponentě (např. "My App / Menu / btn_new").

## Soubory

*   `VytiskniCestu.hs`: Zdrojový kód funkce a definice datového typu `PrvekGUI`.

## Použití

Funkci lze použít pro ladění GUI struktur nebo pro získání informací o umístění konkrétních komponent.
