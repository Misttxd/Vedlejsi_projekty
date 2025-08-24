# OOP CV6 - Dědičnost a polymorfismus (Tvary)

Tato složka demonstruje koncepty dědičnosti a polymorfismu v C++ na příkladu geometrických tvarů.

## Soubory:
- `Tvary (1).cpp`: Definuje základní třídu `Tvar` a odvozené třídy `Kruh`, `Obdelnik` a `Ctverec`. Každá odvozená třída implementuje metody pro výpočet obsahu a obvodu specifické pro daný tvar. Program v `main` funkci vytváří instance těchto tvarů a vypisuje jejich vlastnosti, čímž demonstruje, jak lze s různými tvary pracovat jednotně díky dědičnosti.
- `main.cpp`: Obsahuje další příklad dědičnosti a polymorfismu v kontextu bankovního systému, kde třída `CreditAccount` dědí z `Account` a přepisuje chování pro výběry s úvěrovým limitem. Funkce `main2` slouží k demonstraci této funkcionality.
