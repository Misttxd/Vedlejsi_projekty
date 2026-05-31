# API App

Tato aplikace demonstruje práci s HTTP protokolem (GET a POST požadavky) v mobilním/webovém rozhraní pomocí Ionic frameworku.

## Popis funkčnosti
- **GET Code**: Odesílá GET požadavek na zadanou URL s parametry `user` a `timestamp`. Přijatý base64 token je následně dekódován.
- **SEND Code**: Odesílá POST požadavek s hlavičkou `Authorization: Bearer <token>` a zobrazuje finální zprávu od serveru.
- **Vtipeček**: Automaticky načítá a zobrazuje náhodný vtip z externího API při spuštění.
