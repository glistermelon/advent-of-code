global _println
global _exit
global _read_file_to_buffer
global _uint_to_str
global _allocate_mem
global _qsort
global _str_to_uint

section .rodata
    newline: db 10

section .bss
    fd resq 1
    file_stats resb 64
    file_buffer resq 1
    uint_to_str_buf resb 21

section .text

_println:

    ; rsi: address of message
    
    mov rdx, rsi
_println_label2:
    mov ecx, [rdx]
    jecxz _println_label1
    inc rdx
    jmp _println_label2
_println_label1:
    sub rdx, rsi

    mov rax, 1
    mov rdi, 1
    syscall

    mov rax, 1
    mov rdi, 1
    lea rsi, [rel newline]
    mov rdx, 1
    syscall
    ret

_exit:
    mov rax, 60
    mov rdi, 0
    syscall

_read_file_to_buffer:

    ;  IN rdi: file path
    ; OUT rax: data buffer
    ; OUT rdx: data length (number of bytes)

    mov rax, 2  ; open file
    mov rsi, 0  ; read only
    mov rdx, 0
    syscall

    mov [rel fd], rax

    mov rax, 5           ; file stats
    mov rdi, [rel fd]
    lea rsi, [rel file_stats]
    syscall

    mov rax, 9    ; mmap
    mov rdi, 0
    mov rsi, [rel file_stats + 48]
    inc rsi       ; null terminate
    mov rdx, 3    ; PROT_READ | PROT_WRITE
    mov r10, 0x2  ; MAP_PRIVATE
    mov r8, [rel fd]
    mov r9, 0
    syscall

    mov r15, rax

    mov rax, 3  ; close
    mov rdi, [rel fd]
    syscall

    mov rax, r15
    mov rdx, [rel file_stats + 48]

    ret

_allocate_mem:

    ;  IN rax: size (in bytes)
    ; OUT rax: allocated buffer

    mov rsi, rax ; this is the 2nd argument
    mov rax, 9  ; mmap ;
    mov rdi, 0  ; this is the 1st argument
    mov rdx, 3  ; PROT_READ | PROT_WRITE
    mov r10, 34  ; MAP_PRIVATE | MAP_ANONYMOUS
    mov r8, -1
    mov r9, 0
    syscall
    ret

_uint_to_str:

    ;  IN rax: the integer
    ; OUT rax: the string

    push rax

    ; Initialize the buffer with null bytes
    mov al, 0
    lea rdi, [rel uint_to_str_buf]
    mov rcx, 21
    rep stosb

    pop rax

    lea r8, [rel uint_to_str_buf]
    add r8, 20
_uint_to_str_loop1:

    mov rdx, 0
    mov rcx, 10
    div rcx

    mov [r8], dl
    add byte [r8], 48  ; ASCII '0'
    dec r8

    test ax, ax
    jnz _uint_to_str_loop1

    inc r8
    push r8

    lea rax, [rel uint_to_str_buf]
    add rax, 21
    sub rax, r8

    push rax
    call _allocate_mem
    pop rcx
    pop r8
    lea rsi, [r8]
    mov rdi, rax
    rep movsb
    ret

_qsort:

    ; IN (unmodified) rax: buffer of 64 bit integers
    ; IN (unmodified) rcx: number of integers in the buffer
    
    cmp rcx, 1
    jg _qsort_label1
    ret

_qsort_label1:

    push rcx
    push rax

    push rcx
    push rax
    call _qsort_partition

    mov r8, rax
    pop rax
    push r8
    inc rcx
    call _qsort

    pop rax
    add rax, 8
    mov r8, rcx
    pop rcx
    sub rcx, r8
    call _qsort

    pop rax
    pop rcx

    ret

_qsort_partition:

    ;  IN rax: buffer of 64 bit integers
    ;  IN rcx: number of integers in the buffer
    ; OUT rax: pivot address
    ; OUT rcx: pivot index

    mov r10, rax ; save for later

    push rcx
    dec rcx
    shr rcx, 1
    shl rcx, 3
    add rcx, rax
    mov rdx, [rcx]
    pop rcx

    push rdx

    push rax
    push rax
    mov rax, rcx
    mov rcx, 8
    mul rcx
    mov r9, rax
    pop rcx
    add rcx, r9
    pop rax
    sub rax, 8

    pop rdx

_qsort_partition_loop1:

_qsort_partition_loop2:
    add rax, 8
    mov rbx, [rax]
    cmp rbx, rdx
    jl _qsort_partition_loop2

_qsort_partition_loop3:
    sub rcx, 8
    mov rbx, [rcx]
    cmp rbx, rdx
    jg _qsort_partition_loop3

    cmp rax, rcx
    jge _qsort_partition_loop1_break

    push rdx
    mov rbx, [rax]
    mov rdx, [rcx]
    mov [rax], rdx
    mov [rcx], rbx
    pop rdx

    jmp _qsort_partition_loop1

_qsort_partition_loop1_break:

    mov rax, rcx
    sub rax, r10
    shr rax, 3
    xchg rax, rcx

    ret