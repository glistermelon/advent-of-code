global _start

extern _println
extern _exit
extern _read_file_to_buffer
extern _string_split
extern _linked_list_length
extern _linked_list_data
extern _linked_list_next
extern _uint_to_str
extern _string_to_uint
extern _allocate_mem

%include "string_inc.asm"

section .rodata
    input_file_path: db "../inputs/2.txt", 0
    tab: db 9, 0
    newline: db 10, 0

section .text

_handle_row:

    push r8 ; buffer
    mov r8, 0
    push r8 ; buffer index
    mov r8, -1
    push r8 ; minimum
    mov r8, 0
    push r8 ; maximum

    lea rbx, [rel tab]
    call _string_split
    push rdx

    mov rax, rdx
    call _linked_list_length
    mov rax, rcx
    shl rax, 3
    call _allocate_mem
    mov [rsp + 32], rax
    
_handle_row_loop:

    ; Get string segment
    mov rax, [rsp]
    call _linked_list_data

    ; Null terminate it
    mov rax, [rbx + EXSTR_FIELD_DATA]
    mov rcx, 0
    mov ecx, [rbx + EXSTR_FIELD_LENGTH]
    add rax, rcx
    mov [rax], byte 0

    ; Convert to uint
    mov rax, [rbx + EXSTR_FIELD_DATA]
    call _string_to_uint
    
    ; Maybe update minimum
    cmp rax, [rsp + 16]
    jnc _handle_row_loop_label1
    mov [rsp + 16], rax
_handle_row_loop_label1:

    ; Maybe update maximum
    cmp rax, [rsp + 8]
    jc _handle_row_loop_label2
    jz _handle_row_loop_label2
    mov [rsp + 8], rax
_handle_row_loop_label2:

    ; Save in buffer
    mov rbx, [rsp + 32]
    mov rcx, [rsp + 24]
    mov [rbx + 8 * rcx], rax
    inc rcx
    mov [rsp + 24], rcx

    ; Prepare for next iteration
    pop rax
    call _linked_list_next
    push rax
    test rax, rax
    jnz _handle_row_loop

    ; Prepare part 1 output, clean stack
    add rsp, 8
    pop rax ; max
    sub rax, [rsp]
    add rsp, 8

    ; Part 2

    pop rcx ; buffer len
    pop rbx ; buffer

    push rax ; save for later

    mov rsi, rcx ; former number index
    sub rsi, 2

    mov rdi, rcx ; latter number index
    dec rdi

_handle_row_loop2:

    ; Retrieve numbers
    mov r8, [rbx + 8 * rsi]
    mov r9, [rbx + 8 * rdi]

    ; Ensure r8 >= r9
    cmp r8, r9
    jnc _handle_row_loop2_label1
    xchg r8, r9
_handle_row_loop2_label1:

    ; Divisibility test
    mov rdx, 0
    mov rax, r8
    div r9
    test rdx, rdx
    jz _handle_row_loop2_break

    ; Prepare for next iteration
    dec rdi
    cmp rsi, rdi
    jnz _handle_row_loop2
    dec rsi
    mov rdi, rcx
    dec rdi
    jmp _handle_row_loop2

_handle_row_loop2_break:

    mov rbx, rax
    pop rax

    ret

_solve:

    ; Prepare stack
    mov r8, 0
    push r8 ; output 2
    push r8 ; output 1

    ; Read input
    lea rdi, [rel input_file_path]
    call _read_file_to_buffer

    ; Split into lines
    lea rbx, [rel newline]
    call _string_split
    push rdx

    ; Iterate over lines
_solve_loop:

    ; Get string segment
    mov rax, [rsp]
    call _linked_list_data

    ; Null terminate it
    mov rax, [rbx + EXSTR_FIELD_DATA]
    mov rcx, 0
    mov ecx, dword [rbx + EXSTR_FIELD_LENGTH]
    add rax, rcx
    mov [rax], byte 0

    ; Handle row
    mov rax, [rbx + EXSTR_FIELD_DATA]
    call _handle_row
    add [rsp + 8], rax
    add [rsp + 16], rbx
    
    ; Prepare for next iteration
    pop rax
    call _linked_list_next
    push rax
    test rax, rax
    jnz _solve_loop

    ; Final output, clean stack
    add rsp, 8

    pop rax
    call _uint_to_str
    mov rsi, rax
    call _println

    pop rax
    call _uint_to_str
    mov rsi, rax
    call _println

    ret

_start:
    call _solve
    call _exit