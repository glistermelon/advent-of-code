rm -f ./$1
nasm -f elf64 -g -o $1.o $1.asm
gcc -nostartfiles -g -o $1 $1.o
rm $1.o
./$1