global _start
extern _println
extern _exit
extern _uint_to_str
extern _qsort
extern _str_to_uint

section .data
    input_file_path: db "../inputs/1.txt", 0
    number: db "1234", 0

section .text



_start:
    lea rdi, [rel input_file_path]
    call _read_file_to_buffer
    call _exit