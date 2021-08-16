CV_SW_TOOLCHAIN  ?= /opt/riscv32imaf
RISCV            ?= $(CV_SW_TOOLCHAIN)
RISCV_EXE_PREFIX ?= $(RISCV)/bin/riscv32-unknown-elf-
RISCV_GCC = $(RISCV_EXE_PREFIX)gcc
RISCV_AR = $(RISCV_EXE_PREFIX)ar
SRC = crt0.S handlers.S syscalls.c vectors.S testfloat.c
OBJ = crt0.o handlers.o syscalls.o vectors.o testfloat.o
ELF = testfloat.elf
CFLAGS ?= -O2 -g -mabi=ilp32f -march=rv32imaf -Wall -pedantic -Wl,--gc-sections -ffunction-sections -fdata-sections

all: $(ELF)

$(ELF): $(OBJ) 
	$(RISCV_GCC) $(CFLAGS) -Wl,-T -Wl,riscv32-virt.ld -nostartfiles -o $@ $(OBJ) -lm

%.o : %.c
	$(RISCV_GCC) $(CFLAGS) -c $< -o $@
	
%.o : %.S
	$(RISCV_GCC) $(CFLAGS) -c $< -o $@

clean:
	rm -f $(OBJ) $(LIBCV-VERIF)


vars:
	@echo "make bsp variables:"
	@echo "   CV_SW_TOOLCHAIN  = $(CV_SW_TOOLCHAIN)"
	@echo "   RISCV            = $(RISCV)"
	@echo "   RISCV_EXE_PREFIX = $(RISCV_EXE_PREFIX)"
	@echo "   RISCV_GCC        = $(RISCV_GCC)"

