# HTTP API aplikace

Tato aplikace demonstruje práci s HTTP protokolem v mobilním/webovém rozhraní pomocí Ionic komponent.

## Popis funkčnosti
- **Získání kódu**: Odesílá GET požadavek na zadanou URL s parametry `user` a `timestamp`. Přijatý base64 token je následně dekódován.
- **Odeslání kódu**: Odesílá POST požadavek s hlavičkou `Authorization: Bearer <token>` a zobrazuje finální zprávu od serveru.
- **Vtipeček**: Automaticky načítá a zobrazuje náhodný vtip z externího API při spuštění.
