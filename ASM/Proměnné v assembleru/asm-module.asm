;***************************************************************************
;
; Program for education in subject "Assembly Languages" and "APPS"

;
; Usage of variables in Assembly language
;
;***************************************************************************

    bits 64

    section .data

    extern g_char2
    extern g_short_merged
    extern g_extended

    extern enc_string
    extern g_decoded


    section .text

    global merge_and_extend
    global decode

merge_and_extend:
    mov ah, [g_char2] ;ulozeni prvnich casti do A HIGH
    mov al, [g_char2 + 1] ;ulozeni druhe casti do A LOW
    mov [g_short_merged], ax ;vlozeni A HIGH a A LOW do g short merged (ax = a high a a low zaroven)
    mov [g_extended], ax 

    ret

decode:
    mov al, [ enc_string + 0 ] ; postupne ulozeni jednotlivych casti prvne do registru a pak ze sameho registru do promennych do promennych
    mov [ g_decoded + 0 ], al
    mov al, [ enc_string + 1 ]
    mov [ g_decoded + 1 ], al
    mov al, [ enc_string + 2 ]
    mov [ g_decoded + 2 ], al
    mov al, [ enc_string + 3 ]
    mov [ g_decoded + 3 ], al
    ret

    






