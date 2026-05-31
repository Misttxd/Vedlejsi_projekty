#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <ctype.h>
#include <assert.h>
#include <strings.h>

typedef struct Radky_S //uklada jednotlive radky a slova z puvodniho souboru a dale jejich upravy
{
    char *radek;
    char **slova;
}Radky_S;

typedef struct Slova //uklada jednotlive zadane slova pro operace s databazi
{
    char jednotlive_slova[1000][1000]; //jestli je tohle mozne udelat aby se to dynamicky menilo tak nechapu jak, jelikoz nerozumim jak jde udelat aby se text a jeho delka ulozila do mallocu, kdyz jeste neni odeslany, ale pravdepodobne to je jenom problem v mojich dovednostech
    int pocet;
}Slova;

int vypocet_radku(FILE *input) 
{
    char radek_k_ulozeni[1000] = ""; //stejny problem
    int pocet_radku = 0;
    while (fgets(radek_k_ulozeni, sizeof(radek_k_ulozeni), input) != NULL)
    {
        pocet_radku = pocet_radku + 1;
    }
    rewind(input);
    return pocet_radku;
}

int vypocet_sloupcu(FILE *input)
{
    char radek_k_ulozeni[1000] = ""; //opet
    int pocet_sloupcu = 0;

    if (fgets(radek_k_ulozeni, sizeof(radek_k_ulozeni), input))
    {
        char *token = strtok(radek_k_ulozeni, ","); //rozdeleni obsahu vstupniho souboru na jednotlive slova prvniho radku, cimz se vypocita pocet sloupcu

        while( token != NULL ) 
        {
            pocet_sloupcu ++;
            token = strtok(NULL, ",");
        }
    }
    rewind(input);
    return pocet_sloupcu;
}

Radky_S *ulozeni_radku(FILE *input, Radky_S*radky, int pocet_sloupcu)
{
    char radek_k_ulozeni[1000] = ""; //...
    int index_r = 0; //pro orientaci v jakem radku

    while (fgets(radek_k_ulozeni, sizeof(radek_k_ulozeni), input) != NULL) //uklada jednotlive radky vstupniho souboru do dynamickeho pole (struktury Radky_S)
    { 
        radky[index_r].radek = (char*) malloc(strlen(radek_k_ulozeni)+ 1);
        strcpy(radky[index_r].radek, radek_k_ulozeni);

        char *tok = strtok(radek_k_ulozeni, ",");
        radky[index_r].slova = (char**) calloc(pocet_sloupcu, sizeof(char*));

        for (int indexer_s = 0; tok != NULL; indexer_s++) {
            radky[index_r].slova[indexer_s] = malloc(strlen(tok) + 1);
            strcpy(radky[index_r].slova[indexer_s], tok);
            tok = strtok(NULL, ",");
        }
        index_r ++;
    }
    rewind(input);
    return radky;
}

Slova pocet_slov(char* command_input) //bere vstupni parametry pro funkcnost jednotlivych operaci s databazi (pojmenovani muze byt trochu matouci :)
{
    Slova slova;
    
    slova.pocet = 0;

    char *token_slova = strtok(command_input, " ");
    
    strcpy(slova.jednotlive_slova[slova.pocet], token_slova); //osetreni pro to aby zadany prikaz mohl byt oddelena mezerou, a zbytek carkama
    slova.pocet++;

    if (strcmp(slova.jednotlive_slova[0],"addcol") == 0)
    {
        token_slova= strtok(NULL, " ");
        strcpy(slova.jednotlive_slova[slova.pocet], token_slova);
        slova.pocet++;
    }
    

    token_slova= strtok(NULL, ",");
    while( token_slova != NULL ) 
    {
        strcpy(slova.jednotlive_slova[slova.pocet], token_slova);
        slova.pocet++;
        token_slova = strtok(NULL, ",");
        
    }
    return slova;
}

void free_radky(Radky_S *radky, int pocet_radku, int pocet_sloupcu) //zavola free() na vsechny polozky struktury Radky_S, kde jsou ulozeny prvky databaze
{
    for (int i = 0; i < pocet_radku; i++) {
        if (radky[i].radek != NULL) {
            free(radky[i].radek);
        }
        if (radky[i].slova != NULL) {
            for (int j = 0; j < pocet_sloupcu; j++) {
                if (radky[i].slova[j] != NULL) {
                    free(radky[i].slova[j]);
                }
            }
            free(radky[i].slova);
        }
    }
    free(radky);
}

void operacni_funkce(int pocet_radku, Radky_S *radky) //tokenizace a alokace pameti pro pocetni operace s databazi
{
    for (int i = 0; i < pocet_radku; i++)
    {
        char *temp_radek = (char*) malloc(strlen(radky[i].radek) +1);
        strcpy(temp_radek, radky[i].radek);

        char *token;
        int kolikate_slovo = 0;
        token = strtok(temp_radek, ",");
        
        while( token != NULL ) 
        {
            radky[i].slova[kolikate_slovo] = realloc(radky[i].slova[kolikate_slovo], strlen(token) +1);
            strcpy(radky[i].slova[kolikate_slovo], token);
            token = strtok(NULL, ",");
            kolikate_slovo++;
        }
        free(temp_radek);
    }
    
}

int main(int argc, char const *argv[])
{
    char input_path[100] = "";
    char output_path[100] = "";

    char input[8] = "--input";
    char output[9] = "--output";

    int input_argument_location = 0;
    int output_argument_location = 0;

    //ULOŽENÍ INPUT A OUTPUT PATH DO PROMĚNNÝCH
    

    for (int i = 0; i < argc; i++) 
    {
        if (strcmp(input, argv[i]) == 0) 
        {
            input_argument_location = i;
            if (argc > (i + 1)) 
            {
                if (strcmp(output, argv[i + 1]) != 0)
                {
                    strcpy(input_path, argv[i+ 1]);
                }
                else 
                {
                    printf("Neexistuje nazev souboru po --input.\n");
                    return 1;
                }
            }
            
            else 
            {
                printf("Neexistuje nazev souboru po --input.\n");
                return 1;
            }
        }

        if (strcmp(output, argv[i]) == 0) 
        {
            output_argument_location = i;
            if (argc > (i + 1)) 
            {
                if (strcmp(input, argv[i + 1]) != 0)
                {
                    strcpy(output_path, argv[i+ 1]);
                }
                else 
                {
                    printf("Neexistuje nazev souboru po --output.\n");
                    return 1;
                }
            } 
            
            else 
            {
                printf("Neexistuje nazev souboru po --output.\n");
                return 1;
            }
        }

    }

    if (argc < 5)
    {
        printf("Zadali jste příliš málo argumentů.\n");
        return 1;
    }
    if (argc > 5)
    {
        printf("Zadali jste příliš mnoho argumentů.\n");
        return 1;
    }
    

    //OREVRENI DANYCH SOUBORU
    FILE * input_soubor, *output_soubor;
    input_soubor = fopen(input_path, "rb");
    output_soubor = fopen(output_path, "wt");

    if (input_soubor == NULL) 
    {
        printf("Vstupní soubor neylo možné otevřít\n");
        return 1;
    }

    if (output_soubor == NULL) 
    {
        printf("Výstupní soubor neylo možné otevřít\n");
        return 1;
    }

    
    int pocet_radku = vypocet_radku(input_soubor);
    int pocet_sloupcu = vypocet_sloupcu(input_soubor);



    Radky_S *radky = (Radky_S *)malloc(pocet_radku * sizeof(Radky_S));
    radky = ulozeni_radku(input_soubor, radky, pocet_sloupcu);


    char command_input[999] = ""; //...

    printf("Soubor: %s, sloupce: %d, radky: %d\n", input_path, pocet_sloupcu, pocet_radku-1); //-1 protoze v predloze je ze stejne databaze taky 4 (hadam ze prvni radek s nadpisama se nepocita)
    while (1)
    {
        printf("Zadejte prikaz: ");
        fgets(command_input, sizeof(command_input), stdin);

        Slova slova = pocet_slov(command_input);
                
        
        //ADDROW
        
        if (strcmp(slova.jednotlive_slova[0], "addrow") == 0)
        {
            if (slova.pocet-1 == pocet_sloupcu)
            { 
                radky = realloc(radky,(pocet_radku  + 1)* sizeof(Radky_S));
                if (!radky) {
                    printf("Nepovedlo se alokovat paměť.\n"); //hodne stesti jestli tohle nastane ::DD
                    free_radky(radky, pocet_radku, pocet_sloupcu);
                    fclose(input_soubor);
                    fclose(output_soubor);
                    return 1;
                }

                radky[pocet_radku].radek = (char*) malloc(strlen(slova.jednotlive_slova[1]) + 1);
                strcpy(radky[pocet_radku].radek, slova.jednotlive_slova[1]);
                
                radky[pocet_radku].slova = (char**) malloc(pocet_sloupcu * sizeof(char*));
                
                for (int i = 0; i <slova.pocet - 2; i++) //bez 2 je zahada proc ale funguje to (tohle je cesta do pekel)
                {
                    radky[pocet_radku].slova[i] = malloc(strlen(slova.jednotlive_slova[i + 2]) + 1);
                    sprintf(radky[pocet_radku].radek, "%s,%s", radky[pocet_radku].radek, slova.jednotlive_slova[i + 2]);               
                }
                pocet_radku ++;
                printf("Radek byl pridan.\n");
                
                
            }
            else if ((slova.pocet-1 > pocet_sloupcu))
            {
                printf("Zadáno o %d více parametrů než je počet sloupců\n", (slova.pocet-1) - pocet_sloupcu);
                free_radky(radky, pocet_radku, pocet_sloupcu);
                fclose(input_soubor);
                fclose(output_soubor);
                return 1;
            }
            else if ((slova.pocet-1 != pocet_sloupcu))
            {
                printf("Zadáno o %d méně parametrů než je počet sloupců\n", pocet_sloupcu -(slova.pocet-1));
                free_radky(radky, pocet_radku, pocet_sloupcu);
                fclose(input_soubor);
                fclose(output_soubor);
                return 1;
            }
        }

        if (strcmp(slova.jednotlive_slova[0], "addcol") == 0)
        {
            if (slova.pocet-1 == pocet_radku )
            {
                for (int i = 0; i < pocet_radku; i++)
                {
                    
                    radky[i].radek = realloc(radky[i].radek, strlen(radky[i].radek) + strlen(slova.jednotlive_slova[i+1]) + 3);
                    radky[i].radek[strlen(radky[i].radek)-1] = '\0';
                    sprintf(radky[i].radek, "%s,%s", radky[i].radek, slova.jednotlive_slova[i + 1]);
                    if (i != pocet_radku -1)
                    {
                        radky[i].radek[strlen(radky[i].radek)] = '\n'; //oprava toho aby se na konci nedelalo \n navic, coz nasledne zpusobovalo spatne formatovani vysledne databaze
                    }
                    radky[i].slova = realloc(radky[i].slova, (pocet_sloupcu + 1) * sizeof(char *));
                    radky[i].slova[pocet_sloupcu] = malloc(strlen(slova.jednotlive_slova[i + 1]) + 1);
                    strcpy(radky[i].slova[pocet_sloupcu], slova.jednotlive_slova[i + 1]);
                }
                pocet_sloupcu++;
                printf("Sloupec %s byl pridan.\n", slova.jednotlive_slova[1]);
            }
            
            else if ((slova.pocet-1 > pocet_radku))
            {
                printf("Zadáno o %d více parametrů než je počet řádků\n", (slova.pocet-1) - pocet_radku);
                free_radky(radky, pocet_radku, pocet_sloupcu);
                fclose(input_soubor);
                fclose(output_soubor);
                return 1;
            }
            else if ((slova.pocet-1 != pocet_radku))
            {
                printf("Zadáno o %d méně parametrů než je počet řádků\n", pocet_radku -(slova.pocet-1));
                free_radky(radky, pocet_radku, pocet_sloupcu);
                fclose(input_soubor);
                fclose(output_soubor);
                return 1;
            }
        }

        if (strcmp(slova.jednotlive_slova[0], "average") == 0)
        {
            if (slova.pocet -1 == 1)
            {
                int kde_average = -1;
                float average = 0;

                operacni_funkce(pocet_radku, radky);
                
                for (int i = 0; i < pocet_sloupcu; i++)
                {                    
                    if(strncmp(slova.jednotlive_slova[1], radky[0].slova[i], strlen(radky[0].slova[i]))==0){ //jelikoz kdyz to bylo jenom strcpy tak to porovnavalo i s \n znakem na konci retezce, ktery se tak ulozil po stisknuti enteru pro zadani prikazu, to ale fungovalo se slovem na konci retezce, protoze ten mel kvuli spravnemu formatovani taky na konci \n.
                        kde_average = i;
                    }
                }
                
                if(kde_average == -1)
                {
                    printf("Sloupec `%.*s` nebyl nalezen.\n",(int)strlen(slova.jednotlive_slova[1])-1,slova.jednotlive_slova[1]);
                }
                
                else
                {
                    for (int i = 1; i < pocet_radku; i++)
                    {   
                        average = average + atof(radky[i].slova[kde_average]);
                    }
                    average = average/(pocet_radku-1);
                    printf("Prumer sloupce %.*s: %.1f\n",(int)strlen(slova.jednotlive_slova[1])-1,slova.jednotlive_slova[1] ,average); //velice zacarovane reseni toho, ze na konci zadaneho slova se vyskytuje \n tudiz tisknu pouze jeho delku bez posledniho znaku, coz na stesti funguje i kdyz tam \n neni protoze pak tak je \0

                }
                
                                    
            }    
        }

        if (strcmp(slova.jednotlive_slova[0], "max") == 0)
        {
            if (slova.pocet -1 == 1)
            {
                int kde_max = -1;
                float max = 0;

                operacni_funkce(pocet_radku, radky);

                for (int i = 0; i < pocet_sloupcu; i++)
                {
                    if(strncmp(slova.jednotlive_slova[1], radky[0].slova[i], strlen(radky[0].slova[i]))==0){
                        kde_max = i;
                    }
                }

                if(kde_max == -1)
                {
                    printf("Sloupec `%.*s` nebyl nalezen.\n",(int)strlen(slova.jednotlive_slova[1])-1,slova.jednotlive_slova[1]);
                }
                
                else
                {
                    for (int i = 1; i < pocet_radku; i++)
                    {   
                        if (max <atof(radky[i].slova[kde_max]))
                        {
                            max = atof(radky[i].slova[kde_max]);
                        }
                    }
                    printf("Maximum sloupce %.*s: %.1f\n",(int)strlen(slova.jednotlive_slova[1])-1,slova.jednotlive_slova[1] ,max); //velice zacarovane reseni toho, ze na konci zadaneho slova se vyskytuje \n tudiz tisknu pouze jeho delku bez posledniho znaku, coz na stesti funguje i kdyz tam \n neni protoze pak tak je \0

                }
                
                                    
            }    
        }

        if (strcmp(slova.jednotlive_slova[0], "min") == 0)
        {
            if (slova.pocet -1 == 1)
            {
                
                float min = 0;
                int kde_min = -1;

                operacni_funkce(pocet_radku, radky);

                for (int i = 0; i < pocet_sloupcu; i++)
                {
                    if(strncmp(slova.jednotlive_slova[1], radky[0].slova[i], strlen(radky[0].slova[i]))==0){
                        kde_min = i;
                    }
                }

                if(kde_min == -1)
                {
                    printf("Sloupec `%.*s` nebyl nalezen.\n",(int)strlen(slova.jednotlive_slova[1])-1,slova.jednotlive_slova[1]);
                }
                
                else
                {
                    min = atof(radky[1].slova[kde_min]);
                    for (int i = 1; i < pocet_radku; i++)
                    {   
                        if (min >atof(radky[i].slova[kde_min]))
                        {
                            min = atof(radky[i].slova[kde_min]);
                        }
                    }
                    printf("Minimum sloupce %.*s: %.1f\n",(int)strlen(slova.jednotlive_slova[1])-1,slova.jednotlive_slova[1] ,min); //velice zacarovane reseni toho, ze na konci zadaneho slova se vyskytuje \n tudiz tisknu pouze jeho delku bez posledniho znaku, coz na stesti funguje i kdyz tam \n neni protoze pak tak je \0

                }
                                    
            }    
        }

        if (strcmp(slova.jednotlive_slova[0], "sum") == 0)
        {
            if (slova.pocet -1 == 1)
            {
                int kde_sum = -1;
                float sum = 0;

                operacni_funkce(pocet_radku, radky);

                for (int i = 0; i < pocet_sloupcu; i++)
                {
                    if(strncmp(slova.jednotlive_slova[1], radky[0].slova[i], strlen(radky[0].slova[i]))==0){
                        kde_sum = i;
                    }
                }

                if(kde_sum == -1)
                {
                    printf("Sloupec `%.*s` nebyl nalezen.\n",(int)strlen(slova.jednotlive_slova[1])-1,slova.jednotlive_slova[1]);
                }
                
                else
                {
                    for (int i = 1; i < pocet_radku; i++)
                    {   
                        sum = sum + atof(radky[i].slova[kde_sum]);
                    }
                    printf("Soucet sloupce %.*s: %.f\n",(int)strlen(slova.jednotlive_slova[1])-1,slova.jednotlive_slova[1] ,sum); //velice zacarovane reseni toho, ze na konci zadaneho slova se vyskytuje \n tudiz tisknu pouze jeho delku bez posledniho znaku, coz na stesti funguje i kdyz tam \n neni protoze pak tak je \0

                }
                                    
            }    
        }

        if (strcmp(slova.jednotlive_slova[0], "exit\n") == 0)
        {
            if (slova.pocet -1 == 0)
            {
                printf("Ukladam databazi do souboru %s.\n",output_path);
                for (int i = 0; i < pocet_radku; i++)
                {
                    fprintf(output_soubor, "%s", radky[i].radek);
                }

                free_radky(radky, pocet_radku, pocet_sloupcu);
                fclose(input_soubor);
                fclose(output_soubor);
                return 1;
            }    
        }

        if (strcmp(slova.jednotlive_slova[0], "print\n") == 0)
        {
            if (slova.pocet -1 == 0)
            {   
                printf("\n");
                for (int i = 0; i < pocet_radku; i++)
                {
                    printf("%s", radky[i].radek);
                }
                printf("\n");
            }
            
        }

        if (strcmp(slova.jednotlive_slova[0], "addrow") != 0 && strcmp(slova.jednotlive_slova[0], "addcol") != 0 && strcmp(slova.jednotlive_slova[0], "average") != 0 && strcmp(slova.jednotlive_slova[0], "max") != 0 && strcmp(slova.jednotlive_slova[0], "min") != 0 && strcmp(slova.jednotlive_slova[0], "sum") != 0 && strcmp(slova.jednotlive_slova[0], "exit\n") != 0 && strcmp(slova.jednotlive_slova[0], "print\n") != 0)
        {
            printf("Zadali jste nepodoporovaný příkaz\n"); //hateři budou říkat že to je až moc na tvrdo ošetřené 🤓🤓
        }
    }

    free_radky(radky, pocet_radku, pocet_sloupcu);
    fclose(input_soubor);
    fclose(output_soubor);
    return 0;
}