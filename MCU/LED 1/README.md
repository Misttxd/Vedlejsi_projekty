# Projekt MCU: LED 1 - Ovládání LED diod

Tento projekt demonstruje různé způsoby ovládání LED diod na vývojové desce MCXN-KIT pomocí tlačítek a časovačů (Tickers).

## Funkcionalita

Program ovládá pole 11 LED diod a reaguje na stisknutí tlačítek pro různé světelné efekty.

*   **Ovládání LED**: Řídí pole 11 LED diod připojených k různým pinům.
*   **Vstup z tlačítek**: Čte vstup ze čtyř digitálních tlačítek (BUT1, BUT2, BUT3, BUT4).
*   **Časované události (Tickers)**: Využívá objekty `Ticker` pro plánování funkcí pro periodické spouštění, což umožňuje animace a časované operace.
*   **Funkce CV1 (Tlačítko 1)**: Po stisknutí a uvolnění tlačítka `BUT1` se postupně rozsvěcují jednotlivé LED diody v sekvenci.
*   **Funkce CV2 (Tlačítko 2)**: Pokud je tlačítko `BUT2` drženo, aktuálně vybraná LED dioda (z CV1) bliká. Rychlost blikání je řízena čítačem.
*   **Funkce CV3 (Tlačítko 3 - podobné PWM)**: Pokud je tlačítko `BUT3` drženo, implementuje jednoduchý efekt podobný PWM na aktuálně vybrané LED diodě. Jas (nebo doba svícení) LED diody je řízena proměnnou `seconds_counter`, která se zvyšuje každou sekundu.
*   **`LED_ON_WHEN_END()`**: Pokud nejsou stisknuta tlačítka `BUT2` a `BUT3`, aktuálně vybraná LED dioda svítí.
*   **`seconds_inc()`**: Zvyšuje `seconds_counter` každou sekundu, používá se ve funkci `CV3`.

## Soubory

*   `LED1.cpp`: Hlavní zdrojový soubor obsahující logiku programu.
*   Další soubory (např. `mcxn-kit.h`) jsou externí knihovny pro práci s deskou MCXN-KIT.

## Použití

Projekt je určen pro vývojovou desku MCXN-KIT. Po nahrání firmwaru na desku bude program reagovat na stisknutí tlačítek a ovládat LED diody.
