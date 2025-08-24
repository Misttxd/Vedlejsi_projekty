# Projekt FPR: Přidání komponenty do kontejneru na indexu v GUI

Tento projekt obsahuje Haskell funkci, která přidá novou komponentu do určeného kontejneru v hierarchické struktuře GUI na specifický index.

## Funkcionalita

Funkce `pridejKomponentuDoKontejneruNaIndexu` rekurzivně prochází stromovou strukturu GUI prvků. Pokud najde kontejner se zadaným názvem, vloží do něj novou komponentu na určený index.

## Soubory

*   `PridejKomponentuDoKontejneruNaIndexu.hs`: Zdrojový kód funkce a definice datového typu `PrvekGUI`.

## Použití

Funkci lze použít pro dynamickou modifikaci GUI, například pro vkládání prvků na konkrétní pozice v panelech.
