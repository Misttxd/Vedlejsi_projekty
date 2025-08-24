#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <ctype.h>

char * formatovanecislospodtrzitkemmezikazdymitremiznakyfaktsupernapadloldvojteckaDDDD(int pocet_obchodu) {
    char tmp[11]; //nejdelsi cislo v int muze mit max 10 znaku
    char *formatovany_pocet_obchodu = (char *)malloc(12 * sizeof(char));

    sprintf(tmp, "%d", pocet_obchodu);
    int l = strlen(tmp);

    int index = 0;


    for (int i= 0; i< l; i++) {
        formatovany_pocet_obchodu[index++] = tmp[i];
        
        if ((l- i- 1) % 3== 0 && i != l- 1) {
            formatovany_pocet_obchodu[index++] = '_';
        }
    }
    formatovany_pocet_obchodu[index] = '\0';
    return formatovany_pocet_obchodu;
}

typedef struct Hodnoty
{
    int index_akcie;
    char * nazev_akcie;
    float hodnota_zacatek;
    float hodnota_konec;
    int pocet_obchodu;

    //char formatovany_pocet_obchodu;
}Hodnoty;


int main(int argc, char** argv)
{
    if (argv[2] == NULL){   //ošetření toho když nebude dost argumentů
        printf("Wrong parameters\n");
        return 1;
    }

    char* t = argv[1];
    int  n = atoi(argv[2]); //whatever floats the boat type shi
    Hodnoty *hodnoty = (Hodnoty *)malloc(n * sizeof(Hodnoty)); //mozna to takhle sezere trosililinku vic pameti pri vice vstupech :))))

    //printf("t: %s, n: %d\n", t, n);
    

    char radek[101];

    for (int i = 0; i < n; i++){
        
        fgets(radek, sizeof(radek), stdin);

        strtok(radek, "\n");
        char *pch = strtok(radek, ",");

        int counter = 0;
        while(pch != NULL) {

            switch (counter)
            {
            case 0:
                hodnoty[i].index_akcie = atoi(pch);
                break;
            case 1:
                hodnoty[i].nazev_akcie = (char *)malloc(strlen(pch) + 1);
                if (hodnoty[i].nazev_akcie != NULL)
                {
                    strcpy(hodnoty[i].nazev_akcie, pch);
                }
                break;
            case 2:
                hodnoty[i].hodnota_zacatek = atof(pch);
                break;
            case 3:
                hodnoty[i].hodnota_konec = atof(pch);
                break;
            case 4:
                hodnoty[i].pocet_obchodu = atoi(pch);
                break;
            
            default:
                break;
            }
            
            
            pch = strtok(NULL, ",");
            counter ++;
        }
    }
    Hodnoty header; //inicializace v pripade ze se nevyskytne nic co by se zde ulozilo 
    header.index_akcie = -69;
    header.nazev_akcie = NULL; 
    header.hodnota_zacatek = 0.0;
    header.hodnota_konec = 0.0;
    header.pocet_obchodu = 0;

    for (int i = 0; i<n; i++){
        if ((strcmp(hodnoty[i].nazev_akcie, t) == 0) && (hodnoty[i].pocet_obchodu > header.pocet_obchodu)){
            header.index_akcie = hodnoty[i].index_akcie;
            header.nazev_akcie = hodnoty[i].nazev_akcie;
            header.hodnota_zacatek = hodnoty[i].hodnota_zacatek;
            header.hodnota_konec = hodnoty[i].hodnota_konec;
            header.pocet_obchodu = hodnoty[i].pocet_obchodu;
        }
    }

    if (header.nazev_akcie != NULL){
    printf("<html>\n<body>\n<div>\n<h1>%s: highest volume</h1>\n<div>Day: %d</div>\n<div>Start price: %.2f</div>\n<div>End price: %.2f</div>\n<div>Volume: %s</div>\n</div>\n", t, header.index_akcie, header.hodnota_zacatek, header.hodnota_konec,formatovanecislospodtrzitkemmezikazdymitremiznakyfaktsupernapadloldvojteckaDDDD(header.pocet_obchodu));
    }
    else {
        printf("<html>\n<body>\n<div>\nTicker AMC was not found\n</div>\n");
    }

    //končí pred jednotlivymi TR;
    printf("<table>\n<thead>\n<tr><th>Day</th><th>Ticker</th><th>Start</th><th>End</th><th>Diff</th><th>Volume</th></tr>\n</thead>\n<tbody>\n");
    for (int i = n - 1; i >= 0; i--){
        printf("<tr>\n\t");
        if (strcmp(hodnoty[i].nazev_akcie, t) == 0){
            printf("<td><b>%d</b></td>\n\t", hodnoty[i].index_akcie);
            printf("<td><b>%s</b></td>\n\t", hodnoty[i].nazev_akcie);
            printf("<td><b>%.2f</b></td>\n\t", hodnoty[i].hodnota_zacatek);
            printf("<td><b>%.2f</b></td>\n\t", hodnoty[i].hodnota_konec);
            printf("<td><b>%.2f</b></td>\n\t", hodnoty[i].hodnota_konec - hodnoty[i].hodnota_zacatek);
            printf("<td><b>%s</b></td>\n", formatovanecislospodtrzitkemmezikazdymitremiznakyfaktsupernapadloldvojteckaDDDD(hodnoty[i].pocet_obchodu)); //predelat na string, ktery ma podtrzitka
        }
        else {
            printf("<td>%d</td>\n\t", hodnoty[i].index_akcie);
            printf("<td>%s</td>\n\t", hodnoty[i].nazev_akcie);
            printf("<td>%.2f</td>\n\t", hodnoty[i].hodnota_zacatek);
            printf("<td>%.2f</td>\n\t", hodnoty[i].hodnota_konec);
            printf("<td>%.2f</td>\n\t", hodnoty[i].hodnota_konec - hodnoty[i].hodnota_zacatek);
            printf("<td>%s</td>\n", formatovanecislospodtrzitkemmezikazdymitremiznakyfaktsupernapadloldvojteckaDDDD(hodnoty[i].pocet_obchodu)); //predelat na string, ktery ma podtrzitka
        }
        printf("</tr>\n");
    }
    printf("</tbody>\n</table>\n</body>\n</html>\n");


    return 0;
}
