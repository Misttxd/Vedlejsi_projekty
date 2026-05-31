# Rytmická hra (Rhythm Game)

Semestrální projekt v jazyce Java zaměřený na interaktivní hudební hru inspirovanou populárními rytmickými hrami (např. Guitar Hero nebo osu!mania).

## Popis
Aplikace je vytvořena v JavaFX a umožňuje uživateli hrát skladby trefováním padajících not do rytmu přehrávané hudby. Přesnost stisknutí kláves je měřena a hodnocena. Hra podporuje správu uživatelských profilů a ukládání historických výsledků (skóre, přesnost, datum) do lokální H2 databáze.

## Přehled funkcí
- **Herní engine v reálném čase**: Přehrávání hudebního doprovodu a synchronizovaný pád not podle herní mapy.
- **Detekce přesnosti**: Hodnocení úhozů (Perfect, Great, Good, Miss) podle časové odchylky.
- **Správa hráčů a profilů**: Možnost registrace a přepínání uživatelů.
- **Tabulka skóre (Leaderboard)**: Žebříček nejlepších výsledků načítaný z databáze.
- **Ukládání dat**: Integrovaná perzistentní H2 databáze běžící v embedded režimu.

## Spuštění a sestavení

Pro spuštění a sestavení hry je vyžadován **Maven** a **Java JDK 17** (nebo novější).

### Spuštění hry v dev režimu:
```bash
mvn clean javafx:run
```

### Sestavení redistribuovatelného balíčku (JAR):
```bash
mvn clean package
```
Výsledný zkompilovaný JAR soubor se závislostmi naleznete v adresáři `target/`.
