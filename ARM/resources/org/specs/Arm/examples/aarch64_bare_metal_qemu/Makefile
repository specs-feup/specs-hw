osname := $(shell uname -s)

#use Android toolchain on OS X
#use linaro bare metal toolchain on linux
CROSS_PREFIX=aarch64-none-elf-

all: test64.elf

test64.o: test64.c
	$(CROSS_PREFIX)gcc -c -O0 $< -o $@

startup64.o: startup64.s
	$(CROSS_PREFIX)as -c $< -o $@

test64.elf: test64.o startup64.o
	$(CROSS_PREFIX)ld -Ttest64.ld $^ -o $@

test64.bin: test64.elf
	$(CROSS_PREFIX)objcopy -O binary $< $@

clean:
	rm -f test64.bin test64.elf startup64.o test64.o
