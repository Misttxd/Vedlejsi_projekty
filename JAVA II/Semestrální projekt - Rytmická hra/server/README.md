# Server rytmické hry

Spring Boot server pro semestrální rytmickou hru. Poskytuje REST API a jednoduchou webovou stránku nad daty hry.

## Funkce

- **Hráči**: evidence hráčů přes `PlayerController`.
- **Skladby a mapy**: entity pro skladby a beatmapy.
- **Skóre**: ukládání výsledků a vazba na hráče i mapy.
- **Thymeleaf**: základní domovská stránka s přehledem.

## Spuštění

```powershell
mvn spring-boot:run
```
