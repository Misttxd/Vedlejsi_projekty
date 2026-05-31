# Technologie databázových systémů (TDS)

Tato složka obsahuje semestrální projekt k předmětu Technologie databázových systémů. Projekt navrhuje a implementuje komplexní relační databázovou vrstvu pro rytmickou hru, včetně hráčů, skladeb, herních sezení, achievementů, komentářů, reportů obsahu a historizace změn.

## Popis projektu

Databáze je navržena pro aplikaci, ve které hráči vybírají skladby, hrají jejich obtížnostní varianty, získávají skóre a odemykají achievementy. Součástí projektu je logický a relační model, komplexní Oracle SQL skripty, ukázková data, analytické dotazy a kompletní projektová dokumentace v PDF.

## Obsah složky

- **DDL_script.sql** - Samostatný DDL skript pro vytvoření základní struktury databáze (tabulky, indexy, integritní omezení).
- **TDS_init_data.sql** - Kompletní inicializační skript s mazáním stávajících objektů, vytvořením tabulek, triggerů a vložením rozsáhlých testovacích dat.
- **TDS_commands.sql** - Bohatá sada SQL příkazů, analytických dotazů a úloh nad projektovou databází.
- **dokumentace.pdf** - Kompletní vygenerovaná projektová dokumentace s popisem entit, integritních omezení a business logiky.
- **logicky_model.png** - Logický model databázového schématu.
- **relacni_model.png** - Relační model databázového schématu.
