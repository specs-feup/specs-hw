#
CROSS_PREFIX=riscv32-unknown-elf-

all: test64.elf

test64.o: test64.c
	$(CROSS_PREFIX)gcc -c -O2 $< -o $@

test64.elf: test64.c crt0.s 
	$(CROSS_PREFIX)gcc -g -march=rv32ima -mabi=ilp32 -ffreestanding -O0 -Wl,--gc-sections -nostartfiles -Wl,-T,riscv32-virt.ld $^ -o $@

clean:
	rm -f test64.elf test64.o
