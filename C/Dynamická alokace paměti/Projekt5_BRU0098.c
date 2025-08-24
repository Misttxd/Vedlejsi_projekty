#include <stdio.h> 
#include <stdlib.h> 

char ** mrizka_vytvor(const int rows, const int cols){
    
    char **mrizka = (char**)malloc(rows * sizeof(char *));
    for (int i = 0; i < rows; i++){
        mrizka[i] = (char*)malloc(cols * sizeof(char));
    }
    

    for (int i = 0; i <rows; i++){
        for (int j = 0; j< cols; j++){  
            mrizka[i][j] = '.';
        }
    }
    return mrizka;
}

void mrizka_free(char **mrizka, int rows){
    for (int i = 0; i<rows; i++){
        free(mrizka[i]);
    }
    free(mrizka);
}



void mrizka_print(char **mrizka, int rows, int cols){
    for (int i = 0; i<rows; i++){
        for (int j = 0; j < cols; j++){
            printf("%c", mrizka[i][j]);
        }
        printf("\n");
    }
}


void prava(int zelva_dir[], int pocetZelv){
    for (int i = 0; i<pocetZelv; i++){
        zelva_dir[i] = (zelva_dir[i] + 1) % 4;
    }
}


void leva(int zelva_dir[], int pocetZelv){
    for (int i = 0; i<pocetZelv; i++){
        zelva_dir[i] = (zelva_dir[i] + 3) % 4;
    }
}


void move(int zelva_row[], int zelva_col[], int zelva_dir[], int pocetZelv, int rows, int cols){
    for (int i = 0; i<pocetZelv; i++){
        switch (zelva_dir[i])
        {
        case 0:
            zelva_col[i] = (zelva_col[i] + 1) % cols;
            break;
        case 1:
            zelva_row[i] = (zelva_row[i] + 1) % rows;
            break;
        case 2:
            zelva_col[i] = (zelva_col[i] - 1 + cols) % cols;
            break;
        case 3:
            zelva_row[i] = (zelva_row[i] - 1 + rows) % rows;
            break;
        default:
            break;
        }
    }
}

void draw(char **mrizka, int zelva_row[], int zelva_col[], int pocetZelv){
    for (int i = 0; i<pocetZelv; i++){
        int r = zelva_row[i];
        int c = zelva_col[i];
        if (mrizka[r][c] == '.'){
            mrizka[r][c] = 'o';
        } else{
            mrizka[r][c] = '.';
        }
    }
}

int main()
{
    int rows = 0, cols = 0;

    scanf("%d %d", &rows, &cols);

    char **mrizka = mrizka_vytvor(rows,cols);

    int zelva_row[3] = {0, 0, 0};
    int zelva_col[3] = {0, 0, 0};
    int zelva_dir[3] = {0, 0, 0};
    int pocetZelv = 1;
    

    char znak;
    //printf("r, l, m, o, f, x");
    while (scanf(" %c", &znak)){
        switch (znak)
        {
        case 'r':
            prava(zelva_dir, pocetZelv);
            break;
        case 'l':
            leva(zelva_dir, pocetZelv);
            break;
        case 'm':
            move(zelva_row, zelva_col, zelva_dir, pocetZelv, rows, cols);
            break;
        case 'o':
            draw(mrizka, zelva_row, zelva_col, pocetZelv);
            break;
        case 'f':
            if (pocetZelv < 3) {
                zelva_row[pocetZelv] = 0;
                zelva_col[pocetZelv] = 0;
                zelva_dir[pocetZelv] = 0;
                pocetZelv++;
            }
            break;
        case 'x':
            mrizka_print(mrizka, rows, cols);
            return 0;
            break;
            

        default:
            return 0;
            break;
        }
    }
    mrizka_free(mrizka, rows);
    return 0;
}
