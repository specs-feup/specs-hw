#
CROSS_PREFIX=mb-
all: dotprod.elf

SRCS=dotprod.c
OBJS := ${SRCS:c=o} 

FLAGS=-mlittle-endian -mxl-barrel-shift -mxl-pattern-compare -mno-xl-soft-div -mcpu=v11.0 -mno-xl-soft-mul -mhard-float -mxl-float-convert -mxl-float-sqrt

CCFLAGS=-Wall -O2 -c -I./lib/standalone_microblaze_0/bsp/microblaze_0/include -fmessage-length=0 -Wl,--no-relax -ffunction-sections -fdata-sections -fno-inline-functions -fno-inline-small-functions

CFLAGS=-L./lib/standalone_microblaze_0/bsp/microblaze_0/lib -Wl,--build-id=none -Wl,-T -Wl,./lscript.ld -Wl,--no-relax -Wl,--gc-sections

%.o: %.c
	$(CROSS_PREFIX)gcc $(FLAGS) $(CCFLAGS) $< -o $@

dotprod.elf: $(OBJS)
	$(CROSS_PREFIX)gcc $(FLAGS) $(CFLAGS) $^ -o $@

clean:
	rm -f dotprod.elf dotprod.o

