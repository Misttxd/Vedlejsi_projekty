#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <ctype.h>

int atoi2(char *str){
    int vysledek = 0;

     while (*str >='\x30' &&*str <='\x39') {
        vysledek =(*str-'\x30')+ vysledek *10;  // while porovnava ascii hodnoty znaku
        str++;
    }

    return vysledek;
}

int pocetMezer(char *string){
    int spaceCount = 0;
    for (int i = 0; i < strlen(string); i++){
        if (string[i] == ' '){
            spaceCount++;
        }
    }
    return spaceCount;
}

int pocetLowerCase(char *string){
    int lowerCount = 0;
    for (int i = 0; i < strlen(string); i++){
        if (islower(string[i])){
            lowerCount++;
        }
    }
    return lowerCount;
}
int pocetUpperCase(char *string){
    int upperCount = 0;
    for (int i = 0; i < strlen(string); i++){
        if (isupper(string[i])){
            upperCount++;
        }
    }
    return upperCount;
}



int main()
{
    int n;
    char buf[10] = {0};
    

    fgets(buf, sizeof(buf), stdin);
    n = atoi2(buf);

    //printf("pocet opakovani: %d\n", n);
    if (n > 0){
        for (int o = 0; o<n; o++){
        char retezec[51] = {0};
        char NormRetezec[51] = {0};
        fgets(retezec, sizeof(retezec), stdin);
        
        int j = 0;
        int index = 0;

        for (int i = 0; retezec[i] != '\0'; i++){
            while (retezec[j] == ' '){
                index ++;                               //odstrani zacatecni mezery
                j++;
            }
            NormRetezec[i] = retezec[index + i];
        }

        //printf("bez zacatecnich mezer: %s\n", NormRetezec);
        
        int k= strlen(NormRetezec) -1;
        
        NormRetezec[k] ='\0';
            k--;  
        while (k >= 0 && NormRetezec[k] == ' ') {
                NormRetezec[k] = '\0';
                k--;
            }

        //printf("bez konecnych mezer: %s\n", NormRetezec);

        char NormRezezecDone[51] = {0};
        char *pch = {0};
        strtok(NormRetezec, "\n");
        pch = strtok(NormRetezec, " ");

        
        while(pch != NULL) {
            
            int upper = 0;
            for (int i = 0; i< strlen(pch); i++){
                if (isupper(pch[i])){
                    upper = 1;
                    //break;
                }
            }
            if (upper){
                pch[0] = toupper(pch[0]);
                for(int a = 1; a <strlen(pch); a++){
                    pch[a] = tolower(pch[a]);
                }
            }
            else if (upper == 0){
                for (int a = 0; a <strlen(pch); a++){
                    pch[a] = toupper(pch[a]);
                }
            } 
            else{}
            
            char temp[51] = {0};
            int tempIndex = 0;
            int len = strlen(pch);

            for (int i = 0; i < len; i++) {
                if (i == 0 || pch[i] != pch[i - 1]) { 
                    temp[tempIndex++] = pch[i];
                }
            }
            temp[tempIndex] = '\0';
            strcpy(pch, temp);

            strcat(NormRezezecDone, pch);
            strcat(NormRezezecDone, " \0");
            pch = strtok(NULL, " ");
        }
        NormRezezecDone[strlen(NormRezezecDone)-1] = '\0';

        printf("%s\n", NormRezezecDone);
        printf("lowercase: %d -> %d\n", pocetLowerCase(retezec), pocetLowerCase(NormRezezecDone));
        printf("uppercase: %d -> %d\n", pocetUpperCase(retezec), pocetUpperCase(NormRezezecDone));
        printf("spaces: %d -> %d\n", pocetMezer(retezec), pocetMezer(NormRezezecDone));
        if (o < n-1){
            printf("\n");
        }
    }
    }
    return 0;
}
