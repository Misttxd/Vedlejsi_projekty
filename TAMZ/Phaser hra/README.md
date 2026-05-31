# Phaser hra

Jednoduchá 2D arkádová hra ve Phaser 3 s tilemapou, sbíráním předmětu, nepřítelem a PWA podporou.

## Popis funkčnosti
- Hráč se pohybuje pomocí klávesnice nebo kliknutím/tapnutím na cílové místo.
- Předmět se po sebrání přesune na nové náhodné místo.
- Nepřítel se pohybuje po mapě a při kontaktu vynuluje skóre.
- Hra ukládá nejlepší skóre do lokální paměti prohlížeče.
- Service worker a manifest umožňují offline režim a instalaci jako PWA.

## Spuštění

Hru je vhodné spustit přes lokální webový server, aby fungoval service worker.

```bash
python -m http.server 8000
```

Potom otevřít `http://localhost:8000`.
