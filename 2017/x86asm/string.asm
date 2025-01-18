; struct explicit_string {
;    char* data (0)
;    uint32 length (8)
; }

; The data should still be null terminated.
; The null byte is not included in length.

%define EXSTR_SIZE 12
%define EXSTR_FIELD_DATA 0
%define EXSTR_FIELD_LENGTH 8

global _string_length
global _string_substring
global _string_split

extern _linked_list_new
extern _linked_list_push
extern _allocate_mem

section .rodata

section .text

_string_to_uint:

    ;  IN rax: string (null terminated)
    ; OUT rax: parsed uint

    ; unsafe: rax, rbx, rcx, rdi, rsi, r8

    mov rbx, rax

_string_to_uint_loop1:
    inc rax
    mov rcx, [rax]
    test rcx, rcx
    jnz _string_to_uint_loop1

    mov rcx, rax
    sub rcx, rbx ; rcx = length
    mov rdi, rax
    dec rdi      ; rdi = pointer to last character
    mov rbx, 1   ; rbx = 10^n
    mov rsi, 0   ; output

_string_to_uint_loop2:

    movzx rax, byte [rdi]
    sub rax, 48  ; ASCII '0'
    mul rbx
    add rsi, rax

    dec rdi

    mov r8, rbx
    shl r8, 3
    shl rbx, 1
    add rbx, r8

    loop _string_to_uint_loop2

    mov rax, rsi
    ret

_string_length:

    ;  IN rax: string
    ; OUT rcx: length

    ; unsafe: rax, rcx, rdi

    mov rcx, rax
    mov rdi, rax
    mov rax, 0
_string_length_loop1:
    scasb
    jnz _string_length_loop1
    xchg rcx, rdi
    sub rcx, rdi
    dec rcx
    ret

_string_substring:

    ;  IN rax: string
    ;  IN rbx: substring
    ; OUT rcx: index of substring, or -1

    ; unsafe: rax, rbx, rcx, rdx, r8, xmm0, xmm1

    ; WARNING: currently, substring can be at most 16 characters long

    push rax
    push rbx
    movdqu xmm1, [rbx]
    call _string_length

    ; Ensure string length isn't zero
    test rcx, rcx
    jnz _string_substring_label1
    add rsp, 16
    mov rcx, -1
    ret
_string_substring_label1:

    mov rbx, rcx
    dec rbx
    mov rdx, 0

    pop rax
    call _string_length
    mov r8, 17
    sub r8, rcx

    pop rax

_string_substring_loop1:
    movdqu xmm0, [rax]
    pcmpistri xmm1, xmm0, 2ch
    cmp rcx, r8
    jc _string_substring_loop1_break1
    sub rbx, r8
    jc _string_substring_loop1_break2
    add rax, r8
    add rdx, r8
    jmp _string_substring_loop1
_string_substring_loop1_break2:
    mov rcx, -1
    ret
_string_substring_loop1_break1:
    add rcx, rdx
    ret

_string_split:

    ;  IN rax: string
    ;  IN rbx: splitter string
    ; OUT rdx: linked_list of explicit_string

    ; WARNING: The returned string segments reference the data in the provided string.
    ; Modifying the data in the string may invalidate the result.

    ; Initialize stack

    mov r8, 0
    push r8  ; substring length  (rsp + 24)
    push r8  ; linked list       (rsp + 16)
    push rbx ; substring         (rsp + 8)
    push rax ; string            (rsp + 0)

    call _linked_list_new
    mov [rsp + 16], rax

    mov rax, [rsp + 8]
    call _string_length
    mov [rsp + 24], rcx

_string_split_loop1:

    ; Get index of substring in string
    mov rbx, [rsp + 8]
    mov rax, [rsp]
    call _string_substring
    cmp rcx, -1
    jz _string_split_loop1_break
    test rcx, rcx ; no zero length segments
    jnz _string_split_label1
    pop rax
    inc rax
    push rax

_string_split_label1:

    ; Initialize a new explicit_string
    push rcx
    mov rax, EXSTR_SIZE
    call _allocate_mem
    mov rbx, rax
    pop rcx ; substring index
    pop rax ; string pointer
    mov [rbx + EXSTR_FIELD_DATA], rax
    mov [rbx + EXSTR_FIELD_LENGTH], ecx

    ; Increment string pointer
    add rax, rcx
    add rax, [rsp + 16] ; substring length
    push rax

    ; Push the string to the linked list
    mov rax, [rsp + 16]
    call _linked_list_push

    jmp _string_split_loop1

_string_split_loop1_break:

    ; Add the last string segment
    mov rax, EXSTR_SIZE
    call _allocate_mem
    mov rbx, rax
    mov rax, [rsp] ; string pointer
    call _string_length ; rcx = length
    mov rax, [rsp] ; still string pointer
    mov [rbx + EXSTR_FIELD_DATA], rax
    mov [rbx + EXSTR_FIELD_LENGTH], ecx
    mov rax, [rsp + 16]
    call _linked_list_push

    ; Clean up stack
    mov rdx, [rsp + 16]
    add rsp, 32

    ret