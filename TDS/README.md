# Technologie Databazovych Systemu I (TDS I)

Tato slozka obsahuje semestralni projekt k predmetu Technologie databazovych systemu I. Projekt navrhuje a implementuje databazovou vrstvu pro rytmickou hru, vcetne uzivatelu, hracu, administratoru, skladeb, hernich sezeni, achievementu, komentaru, reportu obsahu a historizace zmen.

## Popis

Databaze je navrzena pro aplikaci, ve ktere hraci vybira skladby, hraji jejich obtiznostni varianty, ziskavaji skore a odemykaji achievementy. Soucasti projektu je logicky a relacni model, Oracle SQL skripty, ukazkova data, dotazy a LaTeX dokumentace.

## Obsah

- **DDL_script.sql** - samostatny DDL skript pro vytvoreni zakladni struktury databaze.
- **TDS_init_data.sql** - kompletni inicializacni skript s mazanim objektu, vytvorenim tabulek, triggeru a vlozenim testovacich dat.
- **TDS_commands.sql** - sada SQL prikazu a ukolovych dotazu nad projektovou databazi.
- **projekt_latex.tex** - zdrojovy kod dokumentace v LaTeXu.
- **dokumentace.pdf** - vygenerovana projektova dokumentace.
- **logicky_model.png** - logicky model databaze.
- **relacni_model.png** - relacni model databaze.

Soubor `DDL_script.sql` slouzi jako samostatny prehled zakladni databazove struktury bez cele inicializacni casti.
