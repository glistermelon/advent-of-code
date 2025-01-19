 global _start
extern _println
extern _exit
extern _uint_to_str
extern _read_file_to_buffer
extern _allocate_mem
extern _string_to_uint

section .rodata
    input_file_path: db "../inputs/3.txt", 0

section .data
    initial_inner_buffer: dq 25, 1, 2, 4, 5, 10, 11, 23, 25, 1
    puzzle_input: dq 0

section .text

; outer square size: [rsp + 8]
; side_length - 1 : [rsp]
; running total : r8
; inner index : r9
; outer index : r10
; distance to previous corner : r11
; distance to next corner : r12
; inner buffer : r13
; outer buffer: r14

; special cases: outer index 0, -1, -2

_next_shell_loop:

    ; Outer shell is the new inner shell
    mov r13, r14

    ; Wrap numbers around the inner buffer
    mov rax, [rsp + 8]
    dec rax
    mov rbx, [r13 + 8 * rax]
    mov [r13 - 8], rbx
    inc rax
    mov rbx, [r13]
    mov [r13 + 8 * rax], rbx

    ; Compute the first outer number
    mov r8, [r13]
    mov rax, [rsp + 8]
    dec rax
    add r8, [r13 + 8 * rax]

    ; Update square data
    add qword [rsp + 8], 8
    add qword [rsp], 2

    ; Push stuff
    push r8
    push r13

    ; Allocate outer buffer
    mov rax, [rsp + 8]
    add rax, 2
    shl rax, 3
    call _allocate_mem
    mov r14, rax
    add r14, 8

    ; Pop the stuff
    pop r13
    pop r8

    ; Set first outer number
    mov [r14], r8

    ; Reset indices
    mov r9, 0
    mov r10, 1

    ; Reset distances
    mov r11, 2
    mov r12, [rsp]
    sub r12, r11

    ; Set loop counter
    mov rcx, [rsp + 8]
    sub rcx, 3

_next_number_loop:

%define ADD_INNER_NUMBER add r8, [r13 + 8 * r9]

    ; Always add the target inner number
    ADD_INNER_NUMBER

    ; Branch
    test r11, r11
    jz _next_number_loop_label1
    cmp r11, 1
    jz _next_number_loop_label2
    cmp r12, 1
    jz _next_number_loop_label3

    ; On edge
    dec r9
    ADD_INNER_NUMBER
    add r9, 2
    ADD_INNER_NUMBER
    jmp _next_number_loop_label1

    ; Leaving corner
_next_number_loop_label2:
    inc r9
    ADD_INNER_NUMBER
    sub r10, 2
    add r8, [r14 + 8 * r10]
    add r10, 2
    jmp _next_number_loop_label1

    ; Going into corner
_next_number_loop_label3:
    dec r9
    ADD_INNER_NUMBER
    inc r9

_next_number_loop_label1:

    ; Check if number is puzzle answer
    mov rax, [rel puzzle_input]
    cmp rax, r8
    jc _next_shell_loop_return_to_start

    ; Save number
    mov [r14 + 8 * r10], r8
    inc r10
    dec rcx
    test rcx, rcx
    jz _next_number_loop_break

    ; Update distances
    inc r11
    sub r12, 1
    jnz _next_number_loop
    mov r11, 0
    mov r12, [rsp]
    jmp _next_number_loop

_next_number_loop_break:

    ADD_INNER_NUMBER
    dec r9
    ADD_INNER_NUMBER
    inc r9
    add r8, [r14]
    mov [r14 + 8 * r10], r8
    inc r10
    
    ADD_INNER_NUMBER
    add r8, [r14]
    mov [r14 + 8 * r10], r8

    jmp _next_shell_loop

_start:

    lea rdi, [rel input_file_path]
    call _read_file_to_buffer
    call _string_to_uint
    mov [rel puzzle_input], rax

    mov r8, 8
    push r8

    mov r8, 2
    push r8

    lea r14, [rel initial_inner_buffer]
    add r14, 8

    jmp _next_shell_loop
_next_shell_loop_return_to_start:
    mov rax, r8
    call _uint_to_str
    mov rsi, rax
    call _println
    
    call _exit