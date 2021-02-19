#
CROSS_PREFIX=mb-
all: dotprod.elf

SRCS=dotprod.c
OBJS := ${SRCS:c=o} 

FLAGS=-mlittle-endian -mxl-barrel-shift -mxl-pattern-compare -mno-xl-soft-div -mcpu=v11.0 -mno-xl-soft-mul -mhard-float -mxl-float-convert -mxl-float-sqrt

CCFLAGS=-Wall -O2 -c -fmessage-length=0 -Wl,--no-relax -ffunction-sections -fdata-sections

CFLAGS=-Wl,-T -Wl,./lscript.ld -Wl,--no-relax -Wl,--gc-sections

%.o: %.c
	$(CROSS_PREFIX)gcc $(FLAGS) $(CCFLAGS) $< -o $@

dotprod.elf: $(OBJS)
	$(CROSS_PREFIX)gcc $(FLAGS) $(CFLAGS) $^ -o $@

clean:
	rm -f dotprod.elf dotprod.o

