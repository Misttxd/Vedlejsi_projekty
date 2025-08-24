# Projekt CUDA 3: Jednoduchá animace

Tento projekt implementuje jednoduchou animaci s využitím technologie CUDA. Simuluje letící helikoptéru, která shazuje míč, jenž následně padá a kutálí se. Animace zahrnuje škálování, rotaci a vkládání obrázků, vše prováděné na GPU.

## Funkcionalita

*   **Pozadí:** Statický obrázek na pozadí (`more.jpg`).
*   **Helikoptéra:** Animovaná helikoptéra (`helicopter.png`), která se pohybuje horizontálně po obrazovce.
*   **Míč:** Míč (`ball.png`) je "shazován" z helikoptéry, jakmile dosáhne středu obrazovky. Míč poté padá, dopadne na "zem" (spodní část obrazovky) a kutálí se horizontálně s rotací.
*   **Transformace obrázků:** Míč prochází škálováním a rotací, které jsou zpracovávány pomocí CUDA jader.
*   **Vkládání obrázků:** Obrázky (helikoptéra, míč) jsou vkládány na pozadí s alfa prolnutím.

## Soubory

*   `main6_hdm_ani.cpp`: Hlavní aplikace, která inicializuje animaci, načítá obrázky, spravuje animační smyčku, aktualizuje pozice objektů (helikoptéra, míč) a zobrazuje animovanou scénu pomocí OpenCV. Zpracovává také uživatelský vstup (klávesa ESC pro ukončení).
*   `cuda6_hdm_ani.cu`: Obsahuje CUDA jádra pro manipulaci s obrázky:
    *   `kernel_insertimage`: Vkládá menší obrázek na větší obrázek s alfa prolnutím.
    *   `kernel_bilin_scale`: Provádí bilineární škálování obrázku.
    *   `kernel_rotate`: Rotuje obrázek kolem jeho středu.
*   `animation.h`: Definuje třídu `Animation`, která zapouzdřuje stav animace a poskytuje metody pro spuštění, aktualizaci (`next`) a zastavení animace.
*   `cuda_img.h`: (Použito z předchozích projektů) Definuje strukturu `CudaImg` pro obrazová data.
*   `ball.png`, `helicopter.png`, `more.jpg`: Obrazové podklady použité v animaci.

## Použití

Program se spouští bez argumentů. Stisknutím klávesy ESC se animace ukončí.
