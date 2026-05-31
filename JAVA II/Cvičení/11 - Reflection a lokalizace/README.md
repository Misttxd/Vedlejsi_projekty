# Reflection a lokalizace

Cvičení rozšiřuje JavaFX hru o univerzální editační dialog založený na Java Reflection a lokalizaci textů.

## Funkce

- **Reflection editor**: dialog načítá JavaBean properties přes `Introspector` a `PropertyDescriptor`.
- **Anotace `@MyEdit`**: určuje viditelnost a režim pouze pro čtení u editovaných vlastností.
- **ResourceBundle**: popisky vlastností jsou připravené pro angličtinu a češtinu.
- **Skóre v JPA**: skóre je uložitelné jako entita a enum obtížnosti se ukládá textově.
