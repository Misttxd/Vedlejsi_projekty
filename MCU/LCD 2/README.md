# Projekt MCU: LCD 2 - Interaktivní grafika a jednoduchá hra

Tento adresář obsahuje dva nezávislé projekty (`lcd_2_1.cpp` a `lcd_2_2.cpp`), které demonstrují pokročilejší ovládání LCD displeje a interakci na vývojové desce MCXN-KIT.

## Projekt 1: `lcd_2_1.cpp` (Hra typu Pong)

Tento projekt implementuje jednoduchou hru podobnou Pongu nebo demonstraci pohybu míče a pádel na LCD. Obsahuje pohybující se kruh (míč) a dvě obdélníková pádla (hráče), která lze ovládat dotykovým vstupem.

### Funkcionalita

*   **Inicializace a kreslení na LCD**: Využívá třídy `Cirkel` a `Rectangle` pro kreslení tvarů.
*   **Dotykový vstup**: Čte dotykové body pro ovládání vertikální pozice levého a pravého pádla.
*   **Pohybující se míč**: Objekt `Cirkel` se pohybuje horizontálně po obrazovce.
*   **Pádla**: Dva objekty `Rectangle` (`Levy_hrac`, `Pravy_hrac`) jsou kresleny po stranách a jejich pozice jsou aktualizovány dotykovým vstupem.
*   **Animace**: Využívá `Ticker` objekty pro periodické volání funkcí pro pohyb míče a aktualizaci pádel.

## Projekt 2: `lcd_2_2.cpp` (Ovládání kruhu tlačítky)

Tento projekt implementuje jednoduchou interaktivní aplikaci, kde lze kruh pohybovat po LCD obrazovce pomocí tlačítek vývojové desky.

### Funkcionalita

*   **Inicializace a kreslení na LCD**: Využívá třídu `Cirkel` pro kreslení a manipulaci s kruhem.
*   **Vstup z tlačítek**: Čte vstup ze čtyř digitálních tlačítek (BUT1, BUT2, BUT3, BUT4).
*   **Interaktivní pohyb kruhu**:
    *   `BUT1`: Pohyb kruhu doleva.
    *   `BUT2`: Pohyb kruhu nahoru.
    *   `BUT3`: Pohyb kruhu doprava.
    *   `BUT4`: Pohyb kruhu dolů.
*   **Animace**: Využívá `Ticker` objekt pro periodické volání funkce `Cirkle_move()` pro aktualizaci pozice kruhu na základě stisknutí tlačítek.

## Soubory

*   `lcd_2_1.cpp`: Zdrojový soubor pro hru typu Pong.
*   `lcd_2_2.cpp`: Zdrojový soubor pro ovládání kruhu tlačítky.
*   Další soubory (např. `lcd_lib.h`, `cts_lib.h`, `mcxn-kit.h`) jsou externí knihovny pro práci s LCD, dotykovou obrazovkou a deskou MCXN-KIT.

## Použití

Projekty jsou určeny pro vývojovou desku MCXN-KIT. Po nahrání příslušného firmwaru na desku bude program reagovat na dotyky na LCD a stisknutí tlačítek.
