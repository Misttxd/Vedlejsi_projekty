#include <stdio.h>

//x == a == sirka
//y == b == vyska
void plny(int a, int b){ //case 0
    for (int y = 0; y < b; y++){
        for (int x = 0; x<a; x++){              //funguje
            printf("x");
        }
        printf("\n");
    }
}

void duty(int a, int b){ //case 1
    for (int y = 0; y<b; y++){
        for (int x = 0; x<a; x++){
            if (x == 0 || x == a-1 || y == 0 || y == b - 1 ){
                printf("x");                                    //funguje
            }
            else{
                printf(" ");
            }
        }
        printf("\n");
    }
}

void plnyCisel(int a, int b){ //case 2
    int index = 0;
    for (int y = 0; y<b; y++){
        for (int x = 0; x<a; x++){
            if (x == 0 || x == a-1 || y == 0 || y == b - 1 ){
                printf("x");                                    //funguje
            }
            else{
                printf("%d", index);
                index++;
                if (index > 9) {
                    index = 0;
                }
            }
        }
        printf("\n");
    }
}


void diagonal1(int a/*, int b*/){ //case 3
    for (int y = 0; y < a; y++) {
        for (int x = 0; x < y; x++) { //funguje
            printf(" ");
        }
        printf("x\n");
    }
}

void diagonal2(int a/*, int b*/) { // case 4
    for (int y = 0; y < a; y++) {
        for (int x = 0; x < a; x++) {
            if (x == a - y - 1) {           //funguje
                printf("x");
            } else {
                printf(" ");
            }
        }
        printf("\n");
    }
}


void trojuhelnik(int a/*, int b*/) { //case 5
    int stred = a - 1;
    
    for (int y = 0; y < a; y++) {
        for (int x = 0; x < 2 * a - 1; x++) {
            if (y == a - 1) {                   //funguje, vratit se
                printf("x");
            } else if (x == stred - y || x == stred + y) {
                printf("x");
            } else {
                printf(" ");
            }
        }
        printf("\n");
    }
}

void T(int a, int b){ //case 6
    //int stred = a / 2;
    for (int x = 0; x < b; x ++){
        if (x == 0){
            for (int y = 0; y < a; y++){
                printf("x");
            }
            printf("\n");                   //funguje
        }
        else {
            for (int y = 0; y < a/2; y++){
                printf(" ");
            }
            printf("x");
            /*for (int y = 0; y < a/2; y++){
                printf(" ");
            }*/
            printf("\n");
        }
    }
}

void H(int a, int b){ //case 7 
    int stred_b = (b/2);
    for (int x = 0; x<b; x ++){
        if (x == stred_b){
            for (int y = 0; y<a; y++){
                printf("x");
            }
        }                                   //FUNGUJE
        
        else {
            printf("x");
            for (int y = 0; y <a - 2; y++){
                printf(" ");
            }
            printf("x");
        }
        printf("\n");
    }
}

void bobux(int a, int b){ //case 9
    for (int y = 0; y<b; y++) {
        for (int x = 0; x < a; x++) {
            if (x == 0 || x == a-1 || y == 0 || y == b - 1 ) {
                printf("x");                                        //snadfunguje
            } else {
                int cislo = (y - 1 + (x - 1) * (b - 2)) % 10;
                printf("%d", cislo);
            }
        }
        printf("\n");
    }
}

int main() {
    int obrazec = 0;
    int a = 0;
    int b = 0;

    //printf("obrazec, hodnota a, hodnota b: ");
    scanf("%d%d%d", &obrazec, &a, &b);

    switch (obrazec){
        case 0:
        plny(a,b);
        break;

        case 1:
        duty(a,b);
        break;
        
        case 2:
        plnyCisel(a,b);
        break;

        case 3:
        diagonal1(a);
        break;
        
        case 4:
        diagonal2(a);
        break;

        case 5:
        trojuhelnik(a);
        break;

        case 6:
        T(a,b);
        break;

        case 7:
        H(a,b);
        break;

        case 9:
        bobux(a,b);
        break;

        default: 
        printf("Neznamy obrazec\n");
        break;
    }
    return 0;
}