# JPA ukládání skóre

Cvičení nahrazuje původní ukládání skóre za JPA perzistenci nad H2 databází.

## Funkce

- **Entita Score**: skóre je mapované jako JPA entita.
- **EntityManager**: repository ukládá, načítá, upravuje a maže záznamy.
- **Persistence konfigurace**: projekt obsahuje `persistence.xml`.
- **Enum jako text**: obtížnost skóre se ukládá čitelně jako textová hodnota.
