# Komunikace klient-server

Cvičení rozšiřuje JavaFX hru o socketovou komunikaci mezi hlavní instancí hry a pozorovateli.

## Funkce

- **Server hry**: hlavní instance odesílá stav světa přes socket.
- **Spectator režim**: další instance mohou zobrazovat stav hry bez vlastní simulace.
- **Serializace entit**: přenositelné třídy jsou připravené pro `Serializable`.
- **Bezpečnější kolekce**: práce s entitami bere v úvahu souběh vláken.
