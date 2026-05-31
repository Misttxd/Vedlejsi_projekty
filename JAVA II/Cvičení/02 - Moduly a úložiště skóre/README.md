# Moduly a úložiště skóre

Cvičení rozděluje původní JavaFX hru na více Maven modulů a odděluje API pro ukládání skóre od konkrétních implementací.

## Obsah

- **java2lab02v1**: upravená hra využívající `ScoreStorageInterface`.
- **score-api**: společné rozhraní, model skóre a výjimka.
- **db-store**: databázová implementace úložiště skóre.
- **fsstore**: souborová implementace úložiště skóre.

## Funkce

- **ServiceLoader**: výběr implementace úložiště bez přímého navázání hry na konkrétní třídu.
- **Maven moduly**: oddělené projekty pro API, databázi, soubory a hru.
- **Ukládání skóre**: podporuje více variant perzistence.
