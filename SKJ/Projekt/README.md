# Object Storage API (S3-like)

Semestrální backendový projekt vytvořený v Pythonu pomocí frameworku FastAPI. Implementuje plně funkční rozhraní typu S3 pro správu bucketů a ukládání souborů.

## Popis
Projekt je RESTful backend, který simuluje chování AWS S3 object storage. Umožňuje uživatelům zakládat kbelíky (buckety), nahrávat do nich soubory (objekty), spravovat jejich metadata a monitorovat objem přenesených a uložených dat pro účely billingu. Všechna metadata jsou uložena v relační databázi SQLite pomocí SQLAlchemy ORM a migrace jsou řízeny nástrojem Alembic.

## Přehled funkcí
- **Správa bucketů**: Vytváření, výpis a mazání bucketů (kbelíků) s kontrolou unikátnosti názvu.
- **Práce s objekty**: Nahrávání, stahování a mazání souborů.
- **Soft Delete**: Logické mazání souborů s možností obnovení nebo permanentního smazání.
- **Billing a limity**: Middleware pro počítání a omezování počtu požadavků a přenesených bajtů.
- **Perzistence dat**: SQLAlchemy ORM s relační databází SQLite pro ukládání metadat.
- **Databázové migrace**: Alembic migrační skripty pro řízení změn schématu.

## Spuštění a instalace

Pro spuštění API je vyžadován **Python 3.9** (nebo novější).

### 1. Příprava virtuálního prostředí:
```bash
python -m venv .venv
source .venv/bin/activate  # Na Windows: .venv\Scripts\activate
```

### 2. Instalace závislostí:
```bash
pip install -r requirements.txt
```

### 3. Aplikace databázových migrací (Alembic):
```bash
alembic upgrade head
```

### 4. Spuštění vývojového serveru:
```bash
uvicorn main:app --reload
```
Po spuštění je interaktivní dokumentace (Swagger UI) dostupná na adrese `http://127.0.0.1:8000/docs`.
