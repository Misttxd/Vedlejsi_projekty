# Java 2 - 2. cvičení v1 - java Modules

V projektu  <https://gitlab.vsb.cz/jez04-vyuka/java2/labs/java2lab02v1.git>

### Příprava

Analyzujte třídu `cz.vsb.fei.java2.lab01.dbstore.ScoreRepository` a z ní vyextrahujte rozhraní `ScoreStorageInterface`,
které bude mít metody pro:
- inicializaci uložiště
- uložení jednoho nebo více skóre
- načtení všech `Score`
- smazání `Score`
- zastavení uložiště

Třída `ScoreRepository` bude implementovat toto nové rozhraní.

Vytvořte třídu `ScoreStorageFactory`, ktera použije návrhová vzor `Singelton` a bude vytářet instanci
třídy `ScoreRepository`, ale bude s ní pracovat jako s `ScoreStorageInterface`.

### API

Nové rozhraní `ScoreStorageInterface` společně se třídou `Score` a `ScoreException` přesuňte to samostatného
maven projektu (ScoreApi, ScoreCommon, ScoreBase, ...). Projekt musí v `module-info` exportovat balík s rozhraním a třídou `Score`.

Pojmenování "projektu" je na několika místech:

- složka kde je projekt umístěn
- jméno maven artifactu
- jméme v pom.xml v tagu project/name
- jméno modulu v module-info.java
- jméno java balíčku kde jsou třídy

Pojmenujte vše tak aby se dali snadno rozlišit jednotlivé projekty/artifacty/moduly/balíčky.

V původním projektu hry musíte nový projekt s rozhraním přidat mezi závislosti v `pom.xml`

### DB Implementace

Vytvořte další nový maven projekt (ScoreDBImpl, DBConnector, DbRepository, ...) a do něj
přesuňte třídu `ScoreRepository`. Projekt musí mít v `pom.xml` závislost na projektu s
rozhraním (API) a na db driver pro h2. Projekt musí v `module-info`
exportovat balík s třídou `ScoreRepository`. V `module-info` použijte také direktivu `provides`
pro nabídnutí třídy, která implementuje rozhraní `ScoreStorageInterface`.

### Propojení

Původní projekt hry bude v `module-info` deklarovat používání rozhraní `ScoreStorageInterface`.
V `pom.xml` už nemusí být závislost na H2 db driveru, ale musí tam být závislost na novém projektu
se třídou `ScoreRepository` (DB Implementace). Upravte třídu `ScoreStorageFactory` tak aby nevytvářela přímo instanci
třídy `ScoreRepository`, ale využila třídu `ServiceLoader` ke získání instance třídy, která implementuje
rozhraní `ScoreStorageInterface`.

Vytvořte další projekt, obdobně, jako projekt se třídou `DBConnctor`, tentokrát se ale skóre budou
ukládat do souboru a ne do databáze. Přidejte projekt jako závislost v projektu s hrou.
Nyní `ServiceLoader` vrací kolekci dvou implementací a lze vybrat (třeba náhodně), kterou použít.

![module graph](https://swi.cs.vsb.cz/.imaging/stk/pop/content/dam/jezek/images/java2-lab02.drawio0/jcr:content/java2-lab02.drawio.png.2025-02-24-14-10-31.png)
