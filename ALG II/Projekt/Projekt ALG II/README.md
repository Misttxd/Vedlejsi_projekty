# Analýza Závislostí Zdrojových Kódů

Nástroj pro analýzu závislostí mezi zdrojovými soubory a určení požadavků na kompilaci.

## Popis
Tento projekt implementuje analyzátor závislostí, který:
1.  Načte graf závislostí (např. `fileA -> fileB`) z textového souboru.
2.  Sestaví graf a jeho transpozici (obrácený graf).
3.  Používá **Prohledávání do hloubky (DFS)** na obráceném grafu k nalezení všech souborů, které závisí na změněném souboru (tranzitivně).

## Výstup
Pro každý soubor vypíše seznam všech ostatních souborů, které je nutné překompilovat, pokud dojde k úpravě původního souboru.
