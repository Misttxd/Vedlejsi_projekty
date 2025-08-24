# Projekt MCU: LED 2 - Pokročilé animace LED diod

Tento adresář obsahuje dva nezávislé projekty (`KNIGHT RIDER.cpp` a `ZELEZNICE.cpp`), které demonstrují pokročilé animace a ovládání LED diod na vývojové desce MCXN-KIT.

## Projekt 1: `KNIGHT RIDER.cpp` (Efekt Knight Rider)

Tento projekt implementuje animaci LED diod ve stylu "Knight Rider", kde se sekvence LED diod rozsvěcuje a poté se vrací zpět, čímž vytváří plynulý efekt. Zahrnuje také ovládání rychlosti a jasu a funkci pro rozšíření "světelné lišty" na dvě LED diody.

### Funkcionalita

*   **Ovládání pole LED**: Ovládá pole 8 LED diod.
*   **Efekt Knight Rider**: Funkce `posun()` vytváří klasický plynulý LED vzor.
*   **Ovládání rychlosti (BUT1)**: Stisknutím `BUT1` se snižuje proměnná `rychlost`, což zrychluje animaci.
*   **Ovládání jasu (BUT2 - podobné PWM)**: Stisknutím `BUT2` se snižuje proměnná `brightness`, která ovlivňuje střídu jednoduchého PWM implementovaného ve funkci `PWM()`, čímž se LED diody zdají být tlumenější.
*   **Ovládání počtu LED (BUT3)**: Stisknutím `BUT3` se přepíná mezi jednou LED diodou a "světelnou lištou" ze dvou LED diod pro plynulý efekt.
*   **Časovače (Tickers)**: Využívá objekty `Ticker` pro správu animace, PWM a kontrolu tlačítek.

## Projekt 2: `ZELEZNICE.cpp` (Simulace železničního přejezdu)

Tento projekt simuluje systém závor železničního přejezdu pomocí LED diod. Má dva hlavní programy, které lze přepínat tlačítky, ovládající různé sady LED diod pro znázornění závor a výstražných světel.

### Funkcionalita

*   **Ovládání LED**: Ovládá pole 8 hlavních LED diod a matici 3x3 RGB LED diod.
*   **Výběr programu (BUT1, BUT2)**: `BUT1` vybírá "Program 1" (zavírání závor) a `BUT2` vybírá "Program 2" (otevírání závor).
*   **`Zavory()` (Závory)**:
    *   **Program 1**: LED diody z obou konců se rozsvěcují směrem ke středu, simulující zavírání závor.
    *   **Program 2**: LED diody od středu se vypínají směrem ven, simulující otevírání závor.
*   **`Blikani_STOP()` (Blikání STOP)**: V Programu 1 blikají specifické RGB LED diody (pravděpodobně červená výstražná světla) na koncích.
*   **`Blikani_GO()` (Blikání GO)**: V Programu 2 blikají jiné RGB LED diody (pravděpodobně zelená světla) uprostřed.
*   **Časovače (Tickers)**: Využívá objekty `Ticker` pro správu animace a časování efektů závor a blikání.

## Soubory

*   `KNIGHT RIDER.cpp`: Zdrojový soubor pro efekt Knight Rider.
*   `ZELEZNICE.cpp`: Zdrojový soubor pro simulaci železničního přejezdu.
*   Další soubory (např. `mcxn-kit.h`) jsou externí knihovny pro práci s deskou MCXN-KIT.

## Použití

Projekty jsou určeny pro vývojovou desku MCXN-KIT. Po nahrání příslušného firmwaru na desku budou programy reagovat na stisknutí tlačítek a ovládat LED diody.
