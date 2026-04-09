# Phaser Game

Jednoduchá 2D arkádová hra vytvořená ve Phaser 3 se sbíráním předmětů, vyhýbáním se nepříteli a podporou PWA.

## Popis funkčnosti
- **Pohyb hráče**: Ovládání pomocí šipek i kliknutí/tapu na cílové místo na mapě.
- **Sbírání předmětu**: Po sebrání předmětu se navýší skóre a předmět se objeví na nové náhodné pozici.
- **Nepřítel**: Nepřátelský objekt se průběžně pohybuje po mapě; při kolizi se skóre resetuje.
- **Skóre a best skóre**: Aktuální skóre i nejlepší dosažené skóre se zobrazují ve hře; nejlepší skóre se ukládá do `localStorage`.
- **Penalizace nečinnosti**: Pokud hráč delší dobu nic nesebere, skóre se postupně snižuje.
- **PWA podpora**: Projekt obsahuje `manifest.json` a `sw.js`, takže je možné hru instalovat a používat i offline.

## Poznámka k aktualizacím
- Po změnách souborů určených do cache je potřeba zvýšit verzi `CACHE_N` v `sw.js`, aby se klientům stáhla nová verze aplikace.