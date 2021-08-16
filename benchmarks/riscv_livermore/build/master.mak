# https://riptutorial.com/makefile/example/21376/building-from-different-source-folders-to-different-target-folders

# global settings for riscv baremetal
ifndef TARGET
$(error TARGET not defined!)
endif

ifndef REFERENCE
$(error REFERENCE not defined!)
endif

SHELL:=/bin/bash
CROSS_PREFIX:=riscv32-unknown-elf-
LDPATH=/media/nuno/HDD/gnu/install/official/riscv/lib/

HARNESS:=../../harness
LIVERMORE:=../../livermore/src
SOURCEDIRS:=$(HARNESS) $(LIVERMORE)
BUILDDIR:=$(shell pwd)/

# Generate the GCC includes parameters by adding -I before each source folder
INCLUDES = $(foreach dir, $(SOURCEDIRS), $(addprefix -I, $(dir)))

# Add this list to VPATH, the place make will look for the source files
VPATH = ../ $(SOURCEDIRS) $(dir $(REFERENCE))

# Create a list of *.c sources in DIRS
SOURCES = $(foreach dir,$(SOURCEDIRS),$(wildcard $(dir)/*.c)) $(REFERENCE)
SOURCESa = $(notdir $(SOURCES))

# Define objects for all sources
OBJS := $(SOURCESa:%.c=%.o)

all: $(TARGET)	

$(TARGET): $(SOURCESa) crt0.s
	$(CROSS_PREFIX)gcc $(INCLUDES) -ffreestanding -specs=nosys.specs -O2 -Wl,--no-relax -Wl,--gc-sections -nostartfiles -Wl,-T,../riscv32-virt.ld $^ -o $@

.PHONY: clean
clean:
	rm -f $(TARGET) $(OBJS)

print-%: ; @echo $*=$($*)

