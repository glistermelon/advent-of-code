rm -f ./$1
nasm -f elf64 -g -o util.o util.asm
nasm -f elf64 -g -o string.o string.asm
nasm -f elf64 -g -o linked_list.o linked_list.asm
nasm -f elf64 -g -o $1.o $1.asm
gcc -nostartfiles -g -o $1 *.o
rm *.o
./$1