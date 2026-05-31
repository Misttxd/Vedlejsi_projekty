# Semestrální projekt - Rytmická hra

Semestrální projekt tvoří JavaFX rytmická hra doplněná o Spring Boot server. Hra pracuje s padajícími notami, obtížnostmi, skóre, combo systémem a komunikací se serverovou částí.

## Funkce

- **JavaFX hra**: herní okno, menu, nastavení, vícejazyčné texty a grafické efekty zásahů.
- **Herní mechaniky**: různé obtížnosti, dlouhé noty, release noty, combo násobič a overdrive.
- **Hudební mapy**: mapy jsou uložené v resources společně s hudebními soubory a popisem skladeb.
- **REST klient**: desktopová aplikace obsahuje klienta pro komunikaci se serverem.
- **Spring server**: samostatná serverová část spravuje hráče, skladby, mapy a skóre.

## Spuštění

Serverová část:

```powershell
cd server
mvn spring-boot:run
```

```powershell
mvn javafx:run
```


