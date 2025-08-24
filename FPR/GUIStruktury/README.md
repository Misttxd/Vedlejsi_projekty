# Projekt FPR: Operace s GUI strukturami

Tento projekt obsahuje kolekci funkcí pro manipulaci s hierarchickými strukturami grafického uživatelského rozhraní (GUI) v Haskellu.

## Funkcionalita

*   `spocitejTlacitka`: Spočítá celkový počet tlačítek v GUI struktuře.
*   `pridejPrvekDoKontejneru`: Přidá novou komponentu do určeného kontejneru v GUI.
*   `seznamJmenTlacitek`: Shromáždí jména všech tlačítek v GUI struktuře.
*   `zmenTextKomponenty`: Změní textovou hodnotu komponenty typu "TextovePole" v GUI.
*   `seznamVsechNazvu`: Shromáždí názvy všech komponent v GUI struktuře.
*   `odstranKomponentyPodleNazvu`: Odstraní komponenty se zadanými názvy z GUI.
*   `seznamVsechTlacitek`: Shromáždí všechny komponenty typu "Tlacitko" z GUI.
*   `odstranVsechnaTlacitka`: Odstraní všechny komponenty typu "Tlacitko" z GUI.
*   `vytiskniCestu`: Najde a vytiskne hierarchickou cestu ke komponentě v GUI.
*   `odstranKomponentuZKontejneruNaIndexu`: Odstraní komponentu z kontejneru na indexu v GUI.
*   `spocitejVyskytKomponent`: Spočítá celkový počet textových polí, tlačítek a kontejnerů v GUI.
*   `pridejKomponentuDoKontejneruNaIndexu`: Přidá novou komponentu do určeného kontejneru na specifický index v GUI.

## Soubory

*   `GUIStruktury.hs`: Zdrojový kód všech funkcí a definice datového typu `PrvekGUI`.

## Použití

Tyto funkce poskytují nástroje pro programovou manipulaci a analýzu GUI struktur.
