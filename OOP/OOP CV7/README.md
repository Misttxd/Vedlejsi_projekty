# OOP CV7 - Abstraktní třídy a čistě virtuální funkce (Tvary)

Tato složka prohlubuje koncepty dědičnosti a polymorfismu v C++ zavedením abstraktních tříd a čistě virtuálních funkcí, opět na příkladu geometrických tvarů.

## Soubory:
- `Tvary.cpp`: Definuje abstraktní základní třídu `Tvar` s čistě virtuálními funkcemi `Obsah()` a `Obvod()`, což zajišťuje, že odvozené třídy (`Kruh`, `Obdelnik`, `Ctverec`) musí tyto metody implementovat. Zahrnuje také virtuální destruktor pro správnou správu paměti. Program v `main` funkci demonstruje polymorfní chování pomocí pole ukazatelů na základní třídu, které odkazují na objekty odvozených tříd.
- `mian.cpp`: Obsahuje další příklad dědičnosti a polymorfismu v kontextu bankovního systému, kde třída `CreditAccount` dědí z `Account` a přepisuje chování pro výběry s úvěrovým limitem. Funkce `main2` slouží k demonstraci této funkcionality.
