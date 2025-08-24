//***************************************************************************
//
// Program for education in subject "Assembly Languages"

//
// Empty project
//
//***************************************************************************

#include <stdio.h>

long g_long_array[5] = { 255, 594, 11, 45678, 321};

char g_char_array[99] = "fsadfasdfasdfsdfsdauifu7fhhvdsfui IO cUIRINvOLuhfighafoadf";
char g_to_replace[1] = "a";
char g_new[1] = "Q";

char g_encoded[] = "onafxn";



int g_counter = 0;
long g_output = 0;

extern void pocitadlo_lichych();
extern void pocitadlo_sudych();
extern void nahrazeni_znaku();
extern void ROT_13();



int main()
{
    pocitadlo_lichych();
    printf("%d\n", g_counter);

    pocitadlo_sudych();
    printf("%ld\n",g_output);

    printf("%s\n", g_char_array);
    nahrazeni_znaku();
    printf("%s\n", g_char_array);

    printf("%s\n", g_encoded);
    ROT_13();
    printf("%s\n", g_encoded);

}
