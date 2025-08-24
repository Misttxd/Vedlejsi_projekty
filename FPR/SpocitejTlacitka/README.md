# Projekt FPR: Počet tlačítek v GUI struktuře

Tento projekt obsahuje Haskell funkci, která spočítá celkový počet tlačítek v hierarchické struktuře GUI komponent.

## Funkcionalita

Funkce `spocitejTlacitka` rekurzivně prochází stromovou strukturu komponent (jako jsou kontejnery, textová pole a tlačítka) a sčítá pouze ty, které jsou typu "Tlacitko".

## Soubory

*   `SpocitejTlacitka.hs`: Zdrojový kód funkce a definice datového typu `Komponenta`.

## Použití

Funkci lze použít pro analýzu struktury GUI nebo pro ověření počtu interaktivních prvků.
