# Projekt CUDA 2: Skládání a překrývání obrázků

Tento projekt demonstruje skládání a překrývání obrázků pomocí technologie CUDA s jednotnou pamětí. Program vezme více vstupních obrázků, zkombinuje první dva vedle sebe a poté překryje následující obrázky na kombinovaný obrázek s efektem průhlednosti. Veškeré zpracování obrazu probíhá na GPU pomocí CUDA jader.

## Funkcionalita

Program načte až pět obrázků. První dva obrázky jsou zkombinovány vedle sebe do jednoho širšího obrázku. Následné obrázky jsou poté překryty na tento kombinovaný obrázek s průhledností (alpha = 0.5). Všechny operace jsou prováděny paralelně na GPU.

## Soubory

*   `main5_unm.cpp`: Hlavní aplikace, která načítá a případně mění velikost obrázků, připravuje datové struktury pro CUDA, volá funkce pro kombinování a překrývání obrázků a zobrazuje konečný výsledek.
*   `cuda5_unm.cu`: Obsahuje dvě CUDA jádra:
    *   `kernel_combine`: Kombinuje dva obrázky vedle sebe.
    *   `kernel_overlay`: Překrývá jeden obrázek na druhý s průhledností.
*   `cuda_img.h`: (Použito z předchozího projektu) Definuje strukturu `CudaImg` pro předávání obrazových dat mezi CPU a GPU.

## Použití

Program se spouští z příkazového řádku s cestami k obrázkům jako argumenty. Příklad:

```bash
./program_name <cesta_k_obrazku1> <cesta_k_obrazku2> [cesta_k_obrazku3] ...
```

*   `<cesta_k_obrazkuX>`: Cesty k vstupním obrázkům (až 5 obrázků).
