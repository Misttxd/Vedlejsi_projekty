import os
import random

# Nastavení
POCET_SOUBORU = 10
POCET_CISEL_V_SOUBORU = 25
MAX_HODNOTA_CISLA = 250
ADRESAR_PRO_DATA = "vstupni_data"

def generuj_data():
    """
    Vygeneruje testovací data pro projekt Slévání Seznamů.
    Vytvoří podadresář a v něm zadaný počet .txt souborů,
    z nichž každý obsahuje seřazený seznam náhodných čísel.
    """
    # Vytvoření adresáře, pokud neexistuje
    if not os.path.exists(ADRESAR_PRO_DATA):
        os.makedirs(ADRESAR_PRO_DATA)
        print(f"Adresář '{ADRESAR_PRO_DATA}' byl vytvořen.")

    # Generování souborů
    for i in range(POCET_SOUBORU):
        file_path = os.path.join(ADRESAR_PRO_DATA, f"{i}.txt")
        try:
            with open(file_path, "w") as f:
                # Vygenerování náhodných čísel a jejich seřazení
                cisla = sorted([random.randint(1, MAX_HODNOTA_CISLA) for _ in range(POCET_CISEL_V_SOUBORU)])
                f.write("\n".join(map(str, cisla)))
        except IOError as e:
            print(f"Chyba při zápisu do souboru {file_path}: {e}")
            return

    print(f"Bylo úspěšně vygenerováno {POCET_SOUBORU} souborů v adresáři '{ADRESAR_PRO_DATA}'.")
    print("Nyní můžete spustit hlavní program.")

if __name__ == "__main__":
    generuj_data()
