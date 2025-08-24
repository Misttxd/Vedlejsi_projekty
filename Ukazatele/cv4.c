#include <stdio.h>

int pocetCifer(int cislo) {
    int pocet = 0;
    do {
        cislo =cislo /10;
        pocet++;
    } while (cislo != 0);
    return pocet;
}

void histogramMain(int *m, int *n, int *invalid, int histogramIndex[]){
    
    for (int i = 0; i <*n; i++){
        int cislo;

        scanf("%d", &cislo);
        
        if ((cislo >= *m) && (cislo <= *m + 8)) {
            histogramIndex[cislo - *m]++;
        } 
        else {
            (*invalid)++;
        }
    }
}

void histogramHorizontalne(int *m, int *n, int histogramIndex[], int *invalid){
    histogramMain(m, n, invalid, histogramIndex);
    int maxPocetC = pocetCifer(*m + 8); 
    for (int i = 0; i<9; i++){


        
        int cislo = *m + i;
        int odsazeni = maxPocetC - pocetCifer(cislo); 

        for (int j = 0; j<odsazeni; j++) {
            printf(" ");
        }
    
        printf("%d", cislo);
        if (histogramIndex[i]>0){
            printf(" ");
            for (int j = 0; j< histogramIndex[i]; j++){
            printf("#");
        }
        }
        printf("\n");
    }
    if (*invalid > 0){
        printf("invalid: ");
        for (int i = 0; i < *invalid; i++){
            printf("#");
        }
        printf("\n");
    }
}

void histogramVertikalne(int *m, int *n, int histogramIndex[], int *invalid){
    histogramMain(m, n, invalid, histogramIndex);
    int maxVyska = 0;
    for (int i= 0; i< 9; i++){
        if (histogramIndex[i] > maxVyska) {
            maxVyska = histogramIndex[i];
        }
        if (*invalid > maxVyska){
            maxVyska = *invalid;
        }
    }
    //printf("max vyska: %d\n", maxVyska);
    for (int vyska = maxVyska; vyska > 0; vyska--){
        for (int i = 0; i < 9; i++){
            if (*invalid > 0){
                if (*invalid >= vyska){
                printf("#");
                }
                else {
                printf(" ");
                }
            }
            
            for (i ; i< 9; i++){
                if (histogramIndex[i] >= vyska) {
                printf("#");
                } else {
                printf(" ");
                }
            }
            printf("\n");
        }
    }
    if (invalid > 0){
            printf("i");
        }
    for (int i = 1; i < 10; i++){
        printf("%d", i);
    }
    printf("\n");
}

int main()
{
    char orientace;
    int n, m;
    int invalid = 0;
    int histogramIndex[9] = {0};
    
    
    scanf(" %c", &orientace);

    scanf("%d %d", &n, &m);

    

    if (orientace == 'v'){
        histogramVertikalne(&m, &n, histogramIndex, &invalid);
    }
    else if (orientace == 'h'){
        histogramHorizontalne(&m, &n, histogramIndex, &invalid);
    }
    else {
        printf("Neplatny mod vykresleni\n");
        return 1;
    }

    return 0;
}