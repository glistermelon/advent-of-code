global _println
global _exit
global _read_file_to_buffer
global _uint_to_str
global _allocate_mem

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

    ; Split rax into edx:eax
    mov rdx, rax
    shr rdx, 32

    mov ecx, 10
    div ecx

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