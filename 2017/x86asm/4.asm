 global _start
extern _println
extern _exit
extern _uint_to_str
extern _string_equals

section .rodata
    input_file_path: db "../inputs/3.txt", 0
    str1: db "0123456789abcdef0123456789abcdef0123456789abcdef0123456789abcdef0123456789abcdef", 0
    str2: db "0123456789abcdef0123456789abcdef0123456789abcdef0123456789abcdef0123456789abcdef", 0

section .text

_start:

    lea rax, [rel str1]
    lea rbx, [rel str2]
    call _string_equals
    mov rax, rcx
    call _uint_to_str
    mov rsi, rax
    call _println
    call _exit