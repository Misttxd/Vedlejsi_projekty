# OOP CV8 - Abstraktní třídy a virtuální destruktory (Tvary)

Tato složka dále rozvíjí koncepty abstraktních tříd a virtuálních funkcí, s důrazem na význam virtuálních destruktorů pro správnou správu paměti při polymorfním použití.

## Soubory:
- `tvary.cpp`: Definuje abstraktní základní třídu `Tvar` s čistě virtuálními funkcemi `Obsah()` a `Obvod()`, a virtuálním destruktorem. Odvozené třídy `Kruh`, `Obdelnik` a `Ctverec` implementují tyto funkce. Program v `main` funkci demonstruje polymorfní chování a správné uvolňování paměti pomocí virtuálního destruktoru při práci s objekty přes ukazatele na základní třídu.
- `main.cpp`: Obsahuje další příklad abstraktních tříd a virtuálních destruktorů v kontextu bankovního systému. Zde je zavedena čistě abstraktní třída `AbstractAccount` a demonstruje se správné volání destruktorů při polymorfním mazání objektů.
