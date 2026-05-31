# JPA vazba 1-N

Jednoduchý JPA projekt vytvořený podle zadání prázdného domácího úkolu. Modeluje hráče a jeho výsledky ve vztahu 1:N.

## Funkce

- **Entita Player**: hráč s přezdívkou a kolekcí skóre.
- **Entita ScoreRecord**: jednotlivý výsledek se skladbou, body a časem vytvoření.
- **Vazba 1:N**: jeden hráč má více výsledků přes `@OneToMany` a `@ManyToOne`.
- **ScoreStorage**: jednoduchá vrstva nad `EntityManager` pro uložení a načtení dat.
- **H2 databáze**: vývojové ukládání přes JPA konfiguraci v `persistence.xml`.
