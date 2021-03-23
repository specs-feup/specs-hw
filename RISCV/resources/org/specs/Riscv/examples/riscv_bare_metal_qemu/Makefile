#
CROSS_PREFIX=riscv32-unknown-elf-

all: test64.elf

test64.o: test64.c
	$(CROSS_PREFIX)gcc -c -O2 $< -o $@

#test64.elf: test64.o /opt/riscv/lib/gcc/riscv32-unknown-elf/10.1.0/../../../../riscv32-unknown-elf/lib/crt0.o /opt/riscv/lib/gcc/riscv32-unknown-elf/10.1.0/crtbegin.o 
#	$(CROSS_PREFIX)ld -lgloss -melf32lriscv -lc -lgcc -L/opt/riscv/lib/gcc/riscv32-unknown-elf/10.1.0 -L/opt/riscv/lib/gcc/riscv32-unknown-elf/10.1.0/../../../../riscv32-unknown-elf/lib -L/opt/riscv/riscv32-unknown-elf/lib $^ -o $@

test64.elf: test64.c crt0.s
	$(CROSS_PREFIX)gcc -g -ffreestanding -O0 -Wl,--gc-sections -nostartfiles -Wl,-T,riscv32-virt.ld $^ -o $@



clean:
	rm -f test64.elf test64.o
