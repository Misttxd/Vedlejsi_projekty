POPIS:
Jednoduchý program pro manipulaci s CSV soubory pomocí jendotlivých operací zmíněných níže v tomto souboru. Pro spuštění programu použijte následující příkaz v adrešáři "build": ./PROJEKT --output output.csv --input input.csv
oba souboury input.csv a output.csv jsou už v totožném adresáři vytvořeny, obsah input souboru jde volně měnit. 
Celý program mi přišel poměrně na jednodušší stránce co se týče funkčnosti, tudíž jsem usoudil, aby to nebylo zbytečně složité, že program zpracuju v jednom .c souboru (projekt.c).
Program jako takový funguje bez problému pro operace, které nezahrnují velké množství textu, jelikož část kódu funguje tak, že je pevně daná velikost polí. Ale zároveň věřím, že to nebude dělat problém v obecné funkčnosti.
Dále se zde bohužel vyskytují pamětové chyby, u kterých se mi nepodařilo vypátrat, co je způsobuje... (celkově se zde pracuje hodně s alokacemi paměti a podařilo se mi do toho docela dost zamotat, funkčnost programu to ale neohrožuje (snad))

OPERACE:
addrow <sloupec0>,<sloupec1>,<sloupec2>,...,<sloupecN> - Přidá nový řádek na konec souboru. - Počet zadaných sloupců musí odpovídat počtu sloupců v databázi. - Hodnoty jednotlivých sloupců musí být oddělené čárkou.

addcol <sloupec> <radek0>,<radek1>,<radek2>,...,<radekN> - Přidá nový sloupec se zadaným názvem. - Počet zadaných řádků musí odpovídat počtu řádků v databázi - Hodnoty jednotlivých řádků musí být oddělené čárkou.

average <nazev-sloupce> - Vypíše průměrnou hodnotu zadaného sloupce. - Hodnoty ve sloupci, které nelze převést na desetinné číslo, jsou považovány za 0.

max <nazev-sloupce> - Vypíše největší hodnotu zadaného sloupce. - Hodnoty ve sloupci, které nelze převést na desetinné číslo, jsou považovány za 0.

min <nazev-sloupce> - Vypíše nejmenší hodnotu zadaného sloupce. - Hodnoty ve sloupci, které nelze převést na desetinné číslo, jsou považovány za 0.

sum <nazev-sloupce> - Vypíše součet hodnot zadaného sloupce. - Hodnoty ve sloupci, které nelze převést na desetinné číslo, jsou považovány za 0.

print - Vypíše do konzole aktuální obsah databáze s úpravami pro přehlednost a ulehčení práce.

exit - Zapíše upravenou databázi na zadanou výstupní cestu a ukončí program.

POŽADAVKY:
Kompílátor jazyka C (dopočuji GCC)
Operační systém Linux (na Windows nebyl testován, ale s největší pravděpodobnosti bude také spustitelný)
