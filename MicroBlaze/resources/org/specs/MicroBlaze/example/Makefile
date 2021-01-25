#
CROSS_PREFIX=mb-
all: dotprod.elf

CCFLAGS= -mlittle-endian -mxl-barrel-shift -mxl-pattern-compare -mno-xl-soft-div -mcpu=v11.0 -mno-xl-soft-mul -mhard-float -mxl-float-convert -mxl-float-sqrt

dotprod.o: dotprod.c
	$(CROSS_PREFIX)gcc $(CCFLAGS) $< -o $@

dotprod.elf: dotprod.c
	$(CROSS_PREFIX)gcc -g -Wl,-T -Wl,./lscript.ld -Wl,--no-relax -Wl,--gc-sections $^ -o $@

clean:
	rm -f dotprod.elf dotprod.o

