# Projekt FPR: Odstranění komponent podle názvu v GUI

Tento projekt obsahuje Haskell funkci, která odstraní komponenty se zadanými názvy z hierarchické struktury GUI.

## Funkcionalita

Funkce `odstranKomponentyPodleNazvu` rekurzivně prochází stromovou strukturu GUI prvků. Pokud název komponenty odpovídá některému ze zadaných názvů k odstranění, komponenta je nahrazena prázdným kontejnerem (pro textová pole a tlačítka) nebo je její obsah vyprázdněn (pro kontejnery).

## Soubory

*   `OdstranKomponentyPodleNazvu.hs`: Zdrojový kód funkce a definice datového typu `PrvekGUI`.

## Použití

Funkci lze použít pro dynamickou modifikaci GUI, například pro skrytí nebo odstranění určitých prvků.
