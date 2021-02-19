#
CROSS_PREFIX=riscv32-unknown-elf-

all: test64.elf

test64.o: test64.c
	$(CROSS_PREFIX)gcc -c -O2 $< -o $@

#LDLIBS=-L . -lnosys

#test64.elf: test64.o crt0.o /opt/riscv/lib/gcc/riscv32-unknown-linux-gnu/10.2.0/crtbegin.o 
#	$(CROSS_PREFIX)ld -lgloss -melf32lriscv -lc -lgcc $(LDLIBS) -L/opt/riscv/lib/gcc/riscv32-unknown-linux-gnu/10.2.0 -L/opt/riscv/riscv32-unknown-linux-gnu/lib $^ -o $@


test64.elf: test64.c crt0.s
	$(CROSS_PREFIX)gcc -L . -g -ffreestanding -specs=nosys.specs -O2 -Wl,--no-relax -Wl,--gc-sections -nostartfiles -Wl,-T,riscv32-virt.ld $^ -o $@



clean:
	rm -f test64.elf test64.o
