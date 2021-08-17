#
CROSS_PREFIX=riscv32-unknown-elf-

all: test64.elf

#<<<<<<< HEAD

test64.elf: test64.c crt0.s
	$(CROSS_PREFIX)gcc -g3 -march=rv32imaf -mabi=ilp32 -ffreestanding -O2 -Wl,--gc-sections -nostartfiles -Wl,-T,riscv32-virt.ld $^ -o $@

#=======
#test64.o: test64.c
#	$(CROSS_PREFIX)gcc -c -O2 $< -o $@
#
#test64.elf: test64.c crt0.s 
#	$(CROSS_PREFIX)gcc -g -march=rv32imaf -mabi=ilp32f -ffreestanding -O0 -Wl,--gc-sections -nostartfiles -Wl,-T,riscv32-virt.ld $^ -o $@
#>>>>>>> 3d170b32f9956632134f38474918913333ba75fd

clean:
	rm -f test64.elf test64.o
