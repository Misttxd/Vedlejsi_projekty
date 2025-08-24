#include <stdio.h>

int main() {
    
    int castka = 9420;

    int pocet5000 = 0;
    int pocet2000 = 0;
    int pocet1000 = 0;
    int pocet500 = 0;
    int pocet200 = 0;
    int pocet100 = 0;

    if (castka >= 5000) {
    pocet5000 = castka / 5000;
    castka = castka % 5000;
    }

    if (castka >= 2000) {
    pocet2000 = castka / 2000;
    castka = castka % 2000;
    }
    
    if (castka >= 1000) {
    pocet1000 = castka / 1000;
    castka = castka % 1000;
    }
    
    if (castka >= 500) {
    pocet500 = castka / 500;
    castka = castka % 500;
    }
    
    if (castka >= 200) {
    pocet200 = castka / 200;
    castka = castka % 200;
    }
    
    if (castka >= 100) {
    pocet100 = castka / 100;
    castka = castka % 100;
    }

    printf("Bankovka 5000: %dx\n", pocet5000);
    printf("Bankovka 2000: %dx\n", pocet2000);
    printf("Bankovka 1000: %dx\n", pocet1000);
    printf("Bankovka 500: %dx\n", pocet500);
    printf("Bankovka 200: %dx\n", pocet200);
    printf("Bankovka 100: %dx\n", pocet100);

    return (0);
    }