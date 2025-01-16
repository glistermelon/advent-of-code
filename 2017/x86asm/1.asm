global _start
extern _println
extern _exit
extern _uint_to_str
extern _read_file_to_buffer

section .rodata
    input_file_path: db "../inputs/1.txt", 0

section .text

_solve_part1:

    lea rdi, [rel input_file_path]
    call _read_file_to_buffer
    mov r8, rax
    mov r9, rax
    inc r9
    mov r10, rax
    mov r11, 0

_solve1_loop1:
    movzx rax, byte [r8]
    movzx rbx, byte [r9]
    cmp ax, bx
    jnz _solve1_label1
    sub rax, 48
    add r11, rax
_solve1_label1:
    cmp r9, r8
    jl _solve1_loop1_break
    inc r8
    inc r9
    mov rax, [r9]
    test rax, rax
    jnz _solve1_loop1
    mov r9, r10
    jmp _solve1_loop1
_solve1_loop1_break:

    mov rax, r11
    call _uint_to_str
    mov rsi, rax
    call _println

    ret

_solve_part2:

    lea rdi, [rel input_file_path]
    call _read_file_to_buffer

    mov r8, rax
    mov r9, rax
    mov r10, rax
    mov r11, 0

    shr rdx, 1
    add r9, rdx

_solve2_loop1:
    movzx rax, byte [r8]
    movzx rbx, byte [r9]
    cmp ax, bx
    jnz _solve2_label1
    sub rax, 48
    add r11, rax
_solve2_label1:
    mov rax, [r8]
    test rax, rax
    jz _solve2_loop1_break
    inc r8
    inc r9
    mov rax, [r9]
    test rax, rax
    jnz _solve2_loop1
    mov r9, r10
    jmp _solve2_loop1
_solve2_loop1_break:

    mov rax, r11
    call _uint_to_str
    mov rsi, rax
    call _println

    ret

_start:
    call _solve_part1
    call _solve_part2
    call _exit