global _start
extern _println
extern _exit
extern _uint_to_str
extern _qsort
extern _str_to_uint
extern _string_substring
extern _string_length
extern _string_split
extern _linked_list_length
extern _linked_list_data
extern _linked_list_next

section .data
    input_file_path: db "apple,banana,,dragonfruit,eggplant", 0
    substr: db ",", 0

section .text

_start:

    lea rax, [rel input_file_path]
    lea rbx, [rel substr]
    call _string_split
    mov rax, rdx

_loop:
    test rax, rax
    jz _break
    call _linked_list_data
    push rax
    push rbx
    mov rsi, [rbx]
    call _println
    pop rbx
    pop rax
    call _linked_list_next
    jmp _loop

_break:

    call _exit