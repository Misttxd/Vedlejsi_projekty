//***************************************************************************
//
// Program for education in subject "Assembly Languages"

//
// Empty project
//
//***************************************************************************

#include <stdio.h>




extern void str_merge_diff(char *arr_in1, char * arr_in2, char *arr_out);
extern long arr_sum_and_return(long * arr1, int l_arr1, long * arr2, int l_arr2, int n);



int main()
{
    char arr_in1[] = "abcccc";
    char arr_in2[] = "abdefc";
    char arr_out[] = "";

    str_merge_diff(arr_in1, arr_in2, arr_out);
    printf("str merge ouutput: %s\n", arr_out);


    long arr1[] = {1,2,3,4,5,6};
    long arr2[] = {6,5,4,3,2,1};
    int l_arr1 = sizeof(arr1);
    int l_arr2 = sizeof(arr2);
    int n = 2;

    printf("arr_sum output: %ld\n", arr_sum_and_return(arr1, l_arr1, arr2, l_arr2, n));

}   
