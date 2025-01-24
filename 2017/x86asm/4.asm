global _start
extern _println
extern _exit
extern _uint_to_str
extern _read_file_to_buffer
extern _allocate_mem
extern _string_to_uint
extern _string_split
extern _string_equals
extern _linked_list_next
extern _linked_list_data
extern _string_split_nullify

section .rodata
    input_file_path: db "../inputs/4.txt", 0
    space: db " ", 0

section .data
    input: db "kvvfl kvvfl olud wjqsqa olud frc", 0

section .text

_test_passphrase:

    ;  IN rax: passphrase

    lea rbx, [rel space]
    call _string_split
    mov rax, rdx
    push rax
    call _string_split_nullify
    pop rax
    call _linked_list_data
    mov rsi, [rbx]
    call _println
    ret

_test_passphrase_loop1:
    ; call _linked_list_data

_start:

    ; Read input
    lea rdi, [rel input_file_path]
    call _read_file_to_buffer
    call _test_passphrase
    call _exit