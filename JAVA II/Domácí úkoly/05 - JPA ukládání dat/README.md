# JPA ukládání dat

Domácí úkol převádí ukládání dat rytmické hry na JPA perzistenci.

## Funkce

- **Entita Score**: výsledky hry jsou uložené jako JPA záznamy.
- **Persistence konfigurace**: `persistence.xml` nastavuje H2 databázi.
- **EntityManager**: repository pracuje s databází přes JPA.
- **Herní data**: zachovává mapy, skóre, obtížnosti a hudební resources.
