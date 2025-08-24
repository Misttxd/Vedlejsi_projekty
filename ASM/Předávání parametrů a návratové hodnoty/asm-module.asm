;***************************************************************************
;
; Program for education in subject "Assembly Languages" and "APPS"

;
; Empty project
;
;***************************************************************************

    bits 64

   
;***************************************************************************

    section .text
    global str_merge_diff
    global str_merge_diff2
    global arr_sum_and_return


str_merge_diff2:
    mov rbx, 0
    mov r8, 0

.loop:
    mov al, [rdi + rbx * 1] ;char z prvniho array do al (8bit)
    cmp al, 0          
    je .done

    cmp al, [rsi + rbx * 1]   ;porovna char z 1. arraye s 1. z 2. arraye
    je .loop_end          

    mov [rdx + r8 * 1], al    
    inc r8                  

.loop_end:
    inc rbx                 
    jmp .loop       

.done:
    mov byte [rdx + r8* 1], 0 
    ret




str_merge_diff:
    mov rbx, 0 ;druhe pole
    mov r9, 0 ;prvni pole
    mov r8, 0 ;vystupni pole


.loop:
    mov al, [rdi + r9 * 1] 
    cmp al, 0
    je .done 

    cmp al, [rsi + rbx *1]
    je .next_char_arr1

.not_equal:
    ;mov [rdx + r8 * 1], al  
    ;inc r8
    
    inc rbx
    cmp al, [rsi + rbx *1]
    je .next_char_arr1

.save:
    mov [rdx + r8 * 1], al  
    inc r8
    inc r9
    mov rbx, 0
    jmp .loop


.next_char_arr1:
    mov rbx, 0
    inc r9             
    jmp .loop 


.done:

    mov byte [rdx + r8* 1], 0 ;ukonceni retezce
    ret




arr_sum_and_return:
    cmp rsi, rcx        
    jle .use_esi    ;pokud jsou delky less or equal, tak dale
    mov rsi, rcx    ; pokud je l_arr1 vetsi, tak se nastavi na delku 2. pole (to musi byt mensi)

.use_esi:
    cmp r8, rsi   ; jestli jeni n prepal mimo delku pole
    jl .valid_index  ; Pokud n < min(délky polí), pokračujeme normálně

    mov rax, 99999999999
    ret ;je prepal tak pryc (99999999999 = chybove hlaseni)

.valid_index:
    mov rax, [rdi + r8 * 8] ; Načtení arr1[n]
    add rax, [rdx + r8 * 8] ; Přičtení arr2[n]
    ret




    



