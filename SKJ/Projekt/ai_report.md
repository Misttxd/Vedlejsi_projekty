# Report o využití AI nástrojů

## Použité AI nástroje
- Google Gemini 3.1 Pro
- GPT-5.3-Codex

## Úvodní použití AI

### 0) Převod zadání a start implementace
**Input (prompt):**
"Předělej mi zadání do čistého Markdownu a navrhni podle něj jednoduchou implementaci object storage služby ve FastAPI."

**Output (co AI vygenerovala):**
- převedené a čitelné zadání v Markdown formátu
- základní plán implementace krok za krokem
- návrh endpointů a datového toku pro soubory a metadata

## Prompt log (Input vs Output)

### 1) Návrh základní implementace
**Input (prompt):**
"Navrhni jednoduchou implementaci object storage služby ve FastAPI podle zadání. Chci minimum, bez složité architektury."

**Output (co AI vygenerovala):**
- návrh endpointů `POST /files/upload`, `GET /files`, `GET /files/{id}`, `DELETE /files/{id}`
- návrh jednoduchého ukládání metadat do `metadata.json`
- návrh ukládání souborů do `storage/<user_id>/<file_id>`

**Co bylo potřeba upravit ručně:**
- doplnit/ověřit praktické detaily testování přes Swagger a curl

---

### 2) Implementace uploadu a metadat
**Input (prompt):**
"Implementuj upload souboru přes multipart/form-data, ulož soubor na disk a vrať metadata `id`, `filename`, `size`."

**Output (co AI vygenerovala):**
- funkční upload endpoint
- generování unikátního `file_id` (UUID)
- zápis metadat do `metadata.json` včetně `user_id`, `path`, `size`, `created_at`

**Co bylo potřeba upravit ručně:**
- průběžné ověření odpovědí endpointu a struktury uložených dat

---

### 3) Oddělení souborů podle uživatele
**Input (prompt):**
"Potřebuji zadávat uživatele, aby se soubory ukládaly do samostatných složek ve storage."

**Output (co AI vygenerovala):**
- API začalo používat dynamický `user_id`
- upload ukládá do `storage/<user_id>/<file_id>`
- přidána validace `user_id`

**Co bylo potřeba upravit ručně:**
- odstranit dočasně přidané endpointy `/users`, které nebyly nutné pro finální scope

---

### 4) Kontrola souladu se zadáním
**Input (prompt):**
"Zkontroluj celý projekt proti zadání a napiš konkrétní nedostatky."

**Output (co AI vygenerovala):**
- kontrola endpointů, metadata struktury a ukládání souborů
- potvrzení, že minimální výstup je splněn
- upozornění na menší nedostatky v dokumentaci/reportu

**Co bylo potřeba upravit ručně:**
- zjednodušit a přeformátovat AI report do přehledné podoby

---

### 5) Příklad pomocného promptu do reportu
**Input (prompt):**
"Vysvětli mi stručně rozdíl mezi minimálním výstupem bez SQLAlchemy a plným řešením s databází."

**Output (co AI vygenerovala):**
- minimum: metadata v JSON, jednodušší nasazení, rychlá implementace
- plné řešení: metadata v SQL DB, lepší škálování, transakce, robustnější práce při souběhu

**Co bylo potřeba upravit ručně:**
- nic zásadního, jen zkrácení odpovědi do odevzdávacího stylu

## Co AI udělala správně
- urychlila návrh i implementaci endpointů
- pomohla s kontrolou souladu se zadáním
- pomohla s laděním toku `user_id` a struktury `storage/<user_id>/<file_id>`

## Co bylo nutné opravit
- zkrátit a zpřehlednit report
- odstranit dočasné endpointy navíc
- doladit finální formulace pro odevzdání

## Shrnutí
AI výrazně zrychlila realizaci i kontrolu projektu. Finální verze byla po průběžných úpravách ponechána v jednoduchém stylu odpovídajícím minimálním požadavkům zadání.

---

## Rozšíření zadání 2 (perzistence + validace)

### 6) Migrace metadat z JSON na SQLAlchemy + Pydantic modely
**Input (prompt):**
"Projeď projekt, zkontroluj staré zadání a implementuj nové zadání: metadata do SQLAlchemy a Pydantic modely pro vstupy/výstupy endpointu."

**Output (co AI vygenerovala):**
- migrace persistence z `metadata.json` na SQLite databázi (`storage.db`) přes SQLAlchemy ORM
- SQLAlchemy model `StoredFile` s poli `id`, `user_id`, `filename`, `path`, `size`, `created_at`
- Pydantic modely pro vstupy (`UserQuery`, `FileIdPath`) a výstupy (`FileMetadataResponse`, `DeleteFileResponse`)
- upravené endpointy `POST /files/upload`, `GET /files`, `GET /files/{id}`, `DELETE /files/{id}`
- jednorázová migrace legacy dat z `metadata.json` do DB při startu aplikace

**Co bylo potřeba upravit ručně:**
- doladit rozsah validace parametrů (patterny pro `user_id` a `id`)
- ověřit, že endpoint pro stažení stále vrací binární obsah souboru (`FileResponse`)

**Jaké chyby AI udělala / rizika:**
- bez SQLAlchemy v `requirements.txt` by aplikace po migraci nenaběhla
- je potřeba pohlídat, že validace path/query parametrů zůstane kompatibilní s klientem (UUID formát)

---

## Jednoduché závěrečné doplnění

### 7) Rychlá finální kontrola před odevzdáním
**Input (prompt):**
"Připrav mi jednoduchý checklist, co mám před odevzdáním zkontrolovat."

**Output (co AI vygenerovala):**
- zkontrolovat, že server startuje bez chyby
- ověřit upload, list, download a delete endpoint
- ověřit, že metadata po uploadu končí v DB
- ověřit, že report obsahuje použité prompty

**Co bylo potřeba upravit ručně:**
- zkrátit checklist jen na body, které byly v zadání povinné

---

### 8) Oprava drobností po kontrole zadání 2
**Input (prompt):**
"Zkontroluj jestli je vše správně implementováno podle nového zadání."

**Output (co AI vygenerovala):**
- nahrazení deprecated `datetime.utcnow()` za `datetime.now(timezone.utc)` (Python 3.12+)
- přidání `pydantic` do `requirements.txt` (používáme ho přímo v importech)
- doplnění `response_class=FileResponse` na `GET /files/{id}` endpoint

**Co bylo potřeba upravit ručně:**
- nic, AI opravila vše sama

## Krátké shrnutí práce s AI
- AI nejvíce pomohla s návrhem endpointů a SQLAlchemy modelem.
- Nejvíce ruční práce bylo v testování endpointů a kontrole chybových stavů.
- Výsledek je jednoduché, funkční řešení odpovídající zadání.

---

## Rozšíření zadání 3 (Alembic migrace, buckety, billing, soft delete)


### 9) Billing migrace a API endpointy
**Input (prompt):**
"Doplň billing na úrovni bucketu a endpointy pro buckety a billing."

**Output (co AI vygenerovala):**
- migrace 2: sloupce `bandwidth_bytes`, `current_storage_bytes`, `ingress_bytes`, `egress_bytes`, `internal_transfer_bytes`
- endpoint `POST /buckets/`
- endpoint `GET /buckets/{bucket_id}/objects/`
- endpoint `GET /buckets/{bucket_id}/billing/`
- update upload/download logiky o účtování přenosů

**Co bylo potřeba upravit ručně:**
- sjednotit testovací scénáře pro interní přenos (`X-Internal-Source: true`)

### 10) Soft delete a třetí migrace
**Input (prompt):**
"Přidej soft delete a uprav listování, ať vrací jen nesmazaná data."

**Output (co AI vygenerovala):**
- migrace 3: sloupec `files.is_deleted`
- `DELETE /files/{id}` nyní nastavuje `is_deleted=True`
- přidán i kompatibilní endpoint `DELETE /objects/{object_id}`
- `GET /files` a list objektů v bucketu filtrují jen `is_deleted=False`

**Co bylo potřeba upravit ručně:**
- nic zásadního, jen ověření dat po soft delete

## Chyby AI v zadání 3 a opravy
- při prvním `alembic upgrade head` AI narazila na chybnou deklaraci header dependency ve FastAPI (`Header` default v `Annotated`), což blokovalo import aplikace z `alembic/env.py`
- oprava: default byl přesunut do signatury parametru (`= False`) a migrace pak proběhly úspěšně

## Výsledek
- v projektu je složka `alembic/versions` se třemi navazujícími migracemi
- migrace se aplikují přes `alembic upgrade head`
- nové endpointy pro buckety, billing a soft delete fungují v runtime testu
