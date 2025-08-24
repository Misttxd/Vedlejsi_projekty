# Projekt FPR: Odstranění komponenty z kontejneru na indexu v GUI

Tento projekt obsahuje Haskell funkci, která odstraní komponentu z určeného kontejneru v hierarchické struktuře GUI na základě jejího indexu.

## Funkcionalita

Funkce `odstranKomponentuZKontejneruNaIndexu` rekurzivně prochází stromovou strukturu GUI prvků. Pokud najde kontejner se zadaným názvem, odstraní z něj komponentu na určeném indexu.

## Soubory

*   `OdstranKomponentuZKontejneruNaIndexu.hs`: Zdrojový kód funkce a definice datového typu `PrvekGUI`.

## Použití

Funkci lze použít pro dynamickou modifikaci GUI, například pro odebrání konkrétních prvků z panelů.
