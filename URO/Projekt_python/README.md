# Task Manager (Python)

Tato aplikace slouží jako jednoduchý **Správce úkolů** (Task Manager) vytvořený v Pythonu s využitím moderní grafické knihovny **CustomTkinter** pro moderní vzhled. 

## Hlavní funkce
- **Přidávání úkolů:** Uživatel může přidat nový úkol pomocí dedikovaného formuláře.
- **Třídění úkolů:** Úkoly lze prohlížet podle kategorií – "Všechny úkoly", "Dnes" a "Důležité".
- **Kategorie a Termíny:** Každý úkol má vlastní název, termín splnění (výběr přes kalendář `CTkDatePicker`) a kategorii (Osobní, Práce, Důležité).
- **Změna motivu:** Aplikace podporuje přepínání mezi světlým (Light) a tmavým (Dark) režimem zobrazení přes menu nastavení.
- **Správa úkolů:** Úkoly lze po dokončení odškrtnout nebo je úplně smazat.

## Spuštění projektu
1. Ujistěte se, že máte nainstalovaný Python 3.
2. Nainstalujte potřebné závislosti ze souboru `requirements.txt`:
   ```bash
   pip install -r requirements.txt
   ```
3. Spusťte hlavní soubor aplikace:
   ```bash
   python main.py
   ```