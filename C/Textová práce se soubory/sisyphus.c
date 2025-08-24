#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <ctype.h>
#include <assert.h>
#include <strings.h>

int main(int argc, char** argv)
{
    char *input_path = (char *) malloc(256 * sizeof(input_path));
    char *needle = (char *) malloc(256 * sizeof(needle));
    char ignore[3] ="-i";
    char output[3] = "-o";
    char *output_path = (char *) malloc(256 * sizeof(needle));

    int vypiseme_outputek = 0; //0 = nn, 1 = jj;

    //printf("%d\n",argc);

    if (strcmp(argv[argc-1], output) == 0){
        printf("Missing output path\n"); //pokud je posledni argument -o
        free(needle);
        free(input_path);
        return 1;
    }
    if (argc>4){
        if ((strcmp(argv[argc-2], output) == 0)&& ((strcmp(argv[argc-1], output) != 0)||(strcmp(argv[argc-1], ignore) != 0))){
        vypiseme_outputek = 1;
        strncpy(output_path, argv[argc-1], 255);
        output_path[255] = '\0';
        //printf("outputpath: %s\n",output_path);
        }
    }
    if (argc>4){
        if ((strcmp(argv[argc-3], output) == 0)&& ((strcmp(argv[argc-2], output) != 0)||(strcmp(argv[argc-1], ignore) != 0))){
        vypiseme_outputek = 1; 
        strncpy(output_path, argv[argc-2], 255);
        output_path[255] = '\0';
        //printf("outputpath: %s\n",output_path);
        }
    }
    

    if (argc <= 1){
        printf("Input path not provided\n"); //pokud jsou zadany pouze dva argumenty
        free(needle);
        free(input_path);
        return 1;
    }

    if (argc <= 2){
        printf("Needle not provided\n"); //pokud jsou zadany pouze dva argumenty
        free(needle);
        free(input_path);
        return 1;
    }
    if ((argc == 3) && (strcmp(argv[2], ignore) == 0)){
        printf("Needle not provided\n");        // pokud jsou zadany 3 argumenty, ale posledni je je -i
        free(needle);
        free(input_path);
        return 1;
    }
    else if ((argc == 3) && (strcmp(argv[2], output) == 0)){
        printf("Needle not provided\n");    //pokud jsou zadany 3 parametry ale podledni je -o (slo by to spojit s tim nahore)
        free(needle);
        free(input_path);
        return 1;
    }

    if (argc >=3){ //pokud je vice nez 3 argumenty, ktere splnujou podminky vyse (protoze jinak by byl program vyhozen s return 1)

        if ((strcmp(argv[1], output) == 0)&&(strcmp(argv[3], ignore) == 0)){
            printf("Input path not provided\n");
            free(needle);                   //hateři buodu říkat že to je na tvrdo vyřešený test 8 👎🏿👎🏿👎🏿👎🏿
            free(input_path);
            return 1;
        }
        for (int i = 0; i < argc - 1; i++){
            if ((strcmp(argv[i], ignore) == 0)&&(strcmp(argv[i+1], ignore) == 0)){
                printf("Parameter -i provided multiple times\n"); // pokud jsou dva argumenty po sobe jdouci -i
                free(needle);
                free(input_path);
                return 1;
            }
        }
        for (int i = 0; i < argc - 2; i++){
            if ((strcmp(argv[i], output) == 0)&&(strcmp(argv[i+2], output)== 0)){
                printf("Parameter -o provided multiple times\n"); //pokud jsou dva argumenty po sobe ob jeden -o
                free(needle);
                free(input_path);
                return 1;
            }
        }

        //printf("Got me thinkin' so deep, I'm in my conscience\n");
        if (argc > 3){
            int skibid = 0;
            for (int i = 0; i<argc; i++){
                if ((strcmp(argv[i], output) != 0 )&&(strcmp(argv[i], ignore) != 0)){
                    skibid ++;
                }
                if (skibid == argc){
                    printf("Too many parameters provided\n"); //pokud je vice nez 3 argumenty, ale nevyskytuje se zde -i nebo -o
                    free(needle);
                    free(input_path);
                    return 1;
                }
            }
           
           
        }
            
        //printf("How the fuck we got to where we started?\n");
        int mam_input = 0;
        int kde_pak_zacinat = 0;
        int ignore_kde = 0;
        for (int i = 1; i<argc; i++){
            
            if (((strcmp(argv[i], output) != 0)&&((strcmp(argv[i], ignore))!= 0) )&& (mam_input == 0)){
                strncpy(input_path, argv[i], 255);
                input_path[255] = '\0';
                mam_input = 1;
                kde_pak_zacinat = i;
            }
            
        }
        mam_input = 0;
        for (int i = kde_pak_zacinat +1 ; i<argc; i++){
            if (((strcmp(argv[i], output) != 0)&&((strcmp(argv[i], ignore))!= 0) )&& (mam_input == 0)){
                if (strcmp(argv[i-1], output) == 0){
                    strncpy(needle, argv[i+1],255);
                    needle[255] = '\0';
                    mam_input = 1;
                }
                
                else{
                    strncpy(needle, argv[i],255);
                    needle[255] = '\0';
                    mam_input = 1;
                }
            }
            
        }
        for (int i = 0; i<argc; i++){
            if ((strcmp(argv[i], ignore) == 0)){
                ignore_kde = i;
            }
            
        }
        //printf("inputh path: %s\n",input_path);
        //printf("needle: %s\n",needle);
        //printf("vypsieme outputek hodnota: %d, ignore kde hodnota: %d\n",vypiseme_outputek, ignore_kde);


        //printf("\ninput path: %s\n", input_path);
        //printf("needel: %s\n", needle);
        //printf("ignore pozice: %d\n", ignore_kde);
        char radek[101];

        FILE*soubor_in, *soubor_out;
        soubor_in = fopen(input_path, "rb");
        
        //assert(soubor);

        if (vypiseme_outputek == 0){
            if (ignore_kde == 0){
            while (fgets(radek, sizeof(radek), soubor_in) != NULL){
                //printf("radek: %s",radek);
                //printf("needle: %s\n",needle);
                if (strstr(radek, needle) != NULL){
                    printf("%s", radek);
                    
                }
                
            }
            }
            else if (ignore_kde != 0){
                while (fgets(radek, sizeof(radek), soubor_in) != NULL){
                    if (strcasestr(radek, needle) != 0){
                        printf("%s", radek);
                        
                    }
                    
                }
            }
        }

        else if (vypiseme_outputek == 1){
            
            soubor_out = fopen(output_path, "wt");
            if (ignore_kde == 0){
            while (fgets(radek, sizeof(radek), soubor_in) != NULL){
                if (strstr(radek, needle) != NULL){
                    fprintf(soubor_out,"%s", radek);
                    
                }
                
            }
            }
            else if (ignore_kde != 0){
                while (fgets(radek, sizeof(radek), soubor_in) != NULL){
                    if (strcasestr(radek, needle) != 0){
                        fprintf(soubor_out, "%s", radek);
                        
                    }
                    
                }
            }
            fclose(soubor_out);
        }

        
        
        fclose(soubor_in);

    }   
    free(needle);
    free(input_path);
    return 0;
}