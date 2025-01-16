rm -f ./$1
nasm -f elf64 -g -o util.o util.asm
nasm -f elf64 -g -o $1.o $1.asm
gcc -nostartfiles -g -o $1 $1.o util.o
rm *.o
./$1