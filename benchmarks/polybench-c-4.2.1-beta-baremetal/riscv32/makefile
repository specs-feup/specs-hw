################################################################################
# Makefile for Polybench adapted for RISC-V 32 baremetal
################################################################################

export KERNEL_OPTIONS=-DDATA_TYPE_IS_FLOAT -DMINI_DATASET
export ROOT=$(shell pwd)
SETS=datamining linear-algebra medley stencils

.PHONY: all clean $(SETS)
all: $(SETS)

$(SETS):
	$(MAKE) -C ../polybenchKernels/$@ all

clean:
	for s in $ $(SETS) ; do \
		$(MAKE) -C ../polybenchKernels/$$s clean && wait ; \
	done
	-$(RM) ./build/*.elf

