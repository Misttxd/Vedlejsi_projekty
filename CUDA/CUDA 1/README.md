# Projekt CUDA 1: Redukce barev obrazu

Tento projekt demonstruje použití technologie CUDA s jednotnou pamětí pro zpracování obrazu. Hlavním účelem je redukce barevných složek (červené, zelené, modré) vstupního obrázku.

## Funkcionalita

Program načte obrázek (pomocí knihovny OpenCV) a provede na něm redukci barev. Uživatel může zadat faktory redukce pro červenou, zelenou a modrou složku. Zpracování obrazu probíhá na GPU pomocí CUDA jádra, což umožňuje paralelní zpracování pixelů.

## Soubory

*   `main4_unm.cpp`: Hlavní aplikace, která načítá obrázky, připravuje data pro CUDA a zobrazuje výsledky. Zpracovává také argumenty příkazového řádku pro název souboru obrázku a faktory redukce barev.
*   `cuda4_unm.cu`: Obsahuje CUDA jádro `kernel_redukce`, které provádí samotnou redukci barev na GPU.
*   `cuda_img.h`: Definuje strukturu `CudaImg` pro předávání obrazových dat mezi CPU a GPU.

## Použití

Program se spouští z příkazového řádku. Příklad:

```bash
./program_name <cesta_k_obrazku> [faktor_cervena] [faktor_zelena] [faktor_modra]
```

*   `<cesta_k_obrazku>`: Cesta k vstupnímu obrázku.
*   `[faktor_cervena]`, `[faktor_zelena]`, `[faktor_modra]`: Volitelné faktory redukce pro jednotlivé barevné složky (hodnoty mezi 0.0 a 1.0). Pokud nejsou zadány, redukce se neprovede.
