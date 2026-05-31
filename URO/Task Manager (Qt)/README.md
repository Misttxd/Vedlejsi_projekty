# Task Manager (Qt)

Desktopová aplikace pro správu úkolů vytvořená v C++ a Qt Widgets. Projekt převádí koncept správce úkolů do nativního Qt rozhraní.

## Popis funkčnosti
- Zobrazuje hlavní okno se záhlavím, postranním panelem a seznamem úkolů.
- Umožňuje přidávat nové úkoly přes samostatný dialog.
- Podporuje filtry **Všechny úkoly**, **Dnes** a **Důležité**.
- Úkoly jsou reprezentované vlastní komponentou `TaskCard`.
- Obsahuje dialog nastavení a přepínání motivu.
- Pracuje s datem splnění a kategorií úkolu.

## Technologie
- C++17
- Qt Widgets
- qmake

## Soubory
- **main.cpp**: Vstupní bod aplikace.
- **mainwindow.\***: Hlavní okno a logika filtrování úkolů.
- **taskdialog.***: Dialog pro vytvoření nového úkolu.
- **settingsdialog.***: Dialog nastavení.
- **taskcard.***: Komponenta pro zobrazení jednoho úkolu.
- **task.h**: Datová struktura úkolu.

## Sestavení a spuštění

Pro spuštění aplikace je vyžadován nainstalovaný framework **Qt 5** (nebo **Qt 6**) s nástrojem **qmake**.

### Sestavení z příkazové řádky:
1. Vygenerujte Makefile pomocí qmake:
   ```bash
   qmake PROJECT.pro
   ```
2. Spusťte sestavení (na Windows přes `nmake` nebo `mingw32-make`, na Linuxu `make`):
   ```bash
   make
   ```
3. Spusťte výsledný spustitelný soubor:
   ```bash
   ./PROJECT
   ```

Aplikaci lze také jednoduše otevřít a spustit v integrovaném vývojovém prostředí **Qt Creator** otevřením souboru `PROJECT.pro`.

