;***************************************************************************
;
; Program for education in subject "Assembly Languages" and "APPS"

;
; Empty project
;
;***************************************************************************

    bits 64

    section .data
    extern g_long_array
    extern g_char_array
    extern g_encoded

    extern g_to_replace
    extern g_new


    extern g_counter
    extern g_output




;***************************************************************************

    section .text
    global pocitadlo_lichych
    global pocitadlo_sudych
    global nahrazeni_znaku
    global ROT_13

pocitadlo_lichych:
    mov ecx , 0 ; ecx - counter
    mov rdx , 0


.back:
    cmp rdx, 5
    jnl .end_for
    mov eax , [ g_long_array + rdx * 8 ]
    and eax , 1 ; if ( g_int_array [ rdx ] & 1 )
    jz .no_odd
    inc ecx
.no_odd :
    inc rdx ; rdx ++
    jmp .back

.end_for:
    neg ecx
    mov [g_counter], ecx
    ret

pocitadlo_sudych:
    mov ecx, 0
    mov rdx, 0

.back:
    cmp rdx, 5 ;15 je velikost pole, idealne to udelat nejak jako array.length
    jnl .end_for ;jump if not less
    mov eax, [g_long_array + rdx * 8]
    and eax, 1
    jz .is_even
    jmp .is_odd

.is_even:
    add ecx, [g_long_array + rdx * 8]
    inc rdx 
    jmp .back

.is_odd:
    inc rdx
    jmp .back

.end_for:
    xor edx, edx
    mov eax, ecx
    mov ecx, 5
    div ecx
    mov [g_output], eax
    ret

nahrazeni_znaku:
    mov rdx , 0
    mov al, [ g_new ]
    mov ah, [g_to_replace]

.back:
    cmp [ g_char_array + rdx ], byte 0
    je .found_0
    cmp [ g_char_array + rdx ], byte ah
    je .replace ; if ( g_char_array [ rdx ] >= ’0’ &&
    inc rdx ; rdx ++
    jmp .back

.replace :
    mov [ g_char_array + rdx ], al
    inc rdx
    jmp .back
    
.found_0 :
    ret

ROT_13:
    mov rdx , 0

.back:

    mov al, [g_encoded + rdx] 
    cmp al, 0
    je .found_0

    cmp al, 'a'
    jl .next
    cmp al, 'z'
    jg .next

    add al, 13
    cmp al, 'z'
    jle .store
    sub al,26


.store:
    mov [g_encoded + rdx], al

.next:
    inc rdx
    jmp .back
    
.found_0:
    ret

    



