# Spring REST API

Spring Boot server pro správu hráčů vytvořený podle zadání osmého cvičení.

## Funkce

- **Entita Player**: hráč se jménem, příjmením a datem narození.
- **JPA repository**: ukládání hráčů přes Spring Data JPA.
- **REST controller**: CRUD endpointy pro hráče a endpoint pro generování testovacích dat.
- **Vlastní dotaz**: metoda pro načtení nejmladších hráčů pomocí `@Query`.
