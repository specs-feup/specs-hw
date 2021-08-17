#
CROSS_PREFIX=riscv32-unknown-elf-

all: test64.elf

test64.elf: test64.c crt0.s
	$(CROSS_PREFIX)gcc -g3 -march=rv32imaf -mabi=ilp32 -ffreestanding -O2 -Wl,--gc-sections -nostartfiles -Wl,-T,riscv32-virt.ld $^ -o $@

clean:
	rm -f test64.elf test64.o
