;***************************************************************************
;
; Program for education in subject "Assembly Languages" and "APPS"

;
; Empty project
;
;***************************************************************************

    bits 64

    section .data

    

   
;***************************************************************************

    section .text
    global palindrome
    global multi_f

palindrome:
    mov r8, 0; length counter
    mov rbx, 0; index - FIRST
    mov rdx, 0; index - LAST

.get_last:
    cmp byte [rdi + rdx * 1], 0
    je .dec2
    inc rdx
    inc r8
    jmp .get_last

.dec2:
    mov rbx, 0
    dec rdx
    dec r8
    jmp .last_found

.last_found:
    
    ;;mov rax, r8
    ;;ret


    cmp rbx, r8
    jge .jj

    mov al, [rdi + rdx *1]
    cmp al, [rdi + rbx *1]
    jne .nn ;NOT EQUAL -> NENI PALI

    inc rbx 
    dec rdx
    
    jmp .last_found

.nn:
    mov eax, 0 ;0 = neni 
    ret
.jj: 
    mov eax, 1 ;1 = je
    ret

multi_f: ;rdi = 5, rsi = 1
    mov rbx,0
    mov rdx, rdi
    mov rax, rdi

.lope:
    
    sub rdx, rsi
    cmp rdx, 1
    je .done
    jb .done
    jl .done

    imul rax, rdx
    jmp .lope
.done:
    ret





    



