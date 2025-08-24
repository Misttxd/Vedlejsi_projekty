# Projekt MCU: LCD 1 - Ovládání LCD a dotykové interakce

Tento projekt demonstruje ovládání LCD displeje a interakci s dotykovým vstupem a tlačítky na vývojové desce MCXN-KIT.

## Funkcionalita

Program inicializuje LCD displej, zobrazí text "C" a "S" a poté v nekonečné smyčce monitoruje dotykový vstup a stisknutí tlačítek. Na základě těchto vstupů mění barvu a kreslí kruh nebo obdélník uprostřed LCD.

*   **Inicializace a kreslení na LCD**: Využívá funkce pro inicializaci LCD, kreslení pixelů, kružnic, obdélníků a zobrazení textu.
*   **Dotykový vstup**: Čte dotykové body z dotykové obrazovky.
*   **Vstup z tlačítek**: Čte vstup ze čtyř digitálních tlačítek (BUT1, BUT2, BUT3, BUT4).
*   **Interaktivní kreslení**:
    *   Stisknutí tlačítek BUT1-BUT4 mění aktuální barvu kreslení (červená, zelená, modrá, bílá).
    *   Dotykem levé části obrazovky (u "C") se nakreslí kruh.
    *   Dotykem pravé části obrazovky (u "S") se nakreslí obdélník.
    *   Dotykem středu obrazovky se překreslí naposledy vybraný tvar s aktuálně zvolenou barvou.

## Soubory

*   `lcd1.cpp`: Hlavní zdrojový soubor obsahující logiku programu.
*   Další soubory (např. `lcd_lib.h`, `cts_lib.h`, `mcxn-kit.h`) jsou externí knihovny pro práci s LCD, dotykovou obrazovkou a deskou MCXN-KIT.

## Použití

Projekt je určen pro vývojovou desku MCXN-KIT. Po nahrání firmwaru na desku bude program reagovat na dotyky na LCD a stisknutí tlačítek.
