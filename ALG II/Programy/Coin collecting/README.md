# Sběr Mincí (Coin Collecting)

Řešení problému sběru mincí pomocí dynamického programování.

## Popis
Hledá cestu s maximálním počtem nasbíraných mincí v mřížce při pohybu pouze doprava a dolů.

## Algoritmus
- Používá 2D tabulku `F[i][j]` pro uložení maximálního počtu mincí dosažitelných v buňce `(i, j)`.
- `F[i][j] = max(F[i-1][j], F[i][j-1]) + C[i][j]`
