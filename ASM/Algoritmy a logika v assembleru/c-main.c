
#include <stdio.h>

extern int palindrome(char * arr_in);
extern long multi_f(int in, int n);


int main()
{
    char slovo[] = "robor";
    printf("Palindrom: %d\n", palindrome(slovo));

    printf("multi_f: %ld\n", multi_f(6, 4));


}   
