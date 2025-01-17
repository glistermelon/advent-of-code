; struct linked_list_node {
;    void* data (0)
;    linked_list_node* next (8)
;    linked_list_node* prev (16)
; }

%define LL_NODE_SIZE 24
%define LL_FIELD_DATA 0
%define LL_FIELD_NEXT 8
%define LL_FIELD_PREV 16

global _linked_list_next
global _linked_list_prev
global _linked_list_new
global _linked_list_push

extern _allocate_mem

section .text

_linked_list_next:

    ;  IN rax: pointer to node
    ; OUT rax: pointer to next node

    mov rax, [rax + LL_FIELD_NEXT]
    ret

_linked_list_prev:

    ;  IN rax: pointer to node
    ; OUT rax: pointer to previous node

    mov rax, [rax + LL_FIELD_PREV]
    ret

_linked_list_new:

    ; OUT rax: pointer to head node
    
    ; unsafe registers: all

    mov rax, LL_NODE_SIZE
    call _allocate_mem
    ret

_linked_list_push:

    ;  IN rax: (linked_list_node*) pointer to node
    ;  IN rbx: (void*) pointer to push
    ; OUT rax: (linked_list_node*) pointer to last node

    ; safe registers: rbx
    ; unsafe registers: all others

    ; If the head node isn't storing any data, just set that one
    mov r8, [rax + LL_FIELD_DATA]
    test r8, r8
    jnz _linked_list_push_loop1
    mov [rax + LL_FIELD_DATA], rbx
    ret

    ; Find the last node
_linked_list_push_loop1:
    mov r8, [rax + LL_FIELD_NEXT]
    test r8, r8
    jz _linked_list_push_loop1_break
    mov rax, r8
    jmp _linked_list_push_loop1
_linked_list_push_loop1_break:

    ; Create a new node and configure data and links
    push rax
    push rbx
    call _linked_list_new
    pop rbx
    pop r8 ; r8 = last node pre-push
    mov [rax + LL_FIELD_DATA], rbx
    mov [rax + LL_FIELD_PREV], r8
    mov [r8 + LL_FIELD_NEXT], rax
    ret