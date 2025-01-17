global _start
extern _println
extern _exit
extern _uint_to_str
extern _qsort
extern _str_to_uint
extern _string_substring
extern _string_length

section .data
    input_file_path: db "../inputs/1.txtapplepearbananacool", 0
    substr: db "uts/1.txtapp", 0

section .text



_start:

    lea rax, [rel input_file_path]
    lea rbx, [rel substr]
    call _string_substring

    mov rax, rcx
    call _uint_to_str
    mov rsi, rax
    call _println

    call _exit