# Builder, Log4j2 a skóre

Cvičení rozšiřuje JavaFX hru s UFO o návrhový vzor Builder, logování a práci se skóre.

## Funkce

- **Builder pro nastavení**: třída `Setting` vytváří konfiguraci hry přes builder a předpřipravený hardcore režim.
- **Log4j2**: aplikace loguje do konzole i souboru s rotací logů.
- **Skóre**: repository ukládá a načítá skóre, včetně tříd `Score` a `ScoreException`.
- **Herní logika**: zachovává původní JavaFX hru s dělem, střelami a UFO entitami.
