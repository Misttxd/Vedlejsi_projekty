# OOP CV5 - Dědičnost a polymorfismus (Auta a Garáž)

Tato složka demonstruje koncepty dědičnosti a polymorfismu v C++ na příkladu systému pro správu aut a garáže.

## Soubory:
- `auto.cpp`: Definuje základní třídu `Auto` a odvozené třídy `sportovniAuto` a `osobniAuto`, které dědí vlastnosti z `Auto` a přidávají specifické atributy. Dále obsahuje třídu `Garaz`, která využívá polymorfismus pro správu různých typů aut. Program v `main` funkci demonstruje vytváření a správu aut v garáži, včetně sledování celkového počtu aut pomocí statického člena.
- `main.cpp`: Obsahuje alternativní příklad demonstrující dědičnost v bankovním systému (třída `PartnerAccount` dědící z `Account`), avšak jeho `main2` funkce není standardním vstupním bodem programu.
