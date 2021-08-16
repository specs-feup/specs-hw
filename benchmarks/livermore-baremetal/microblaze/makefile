################################################################################
# Makefile for Polybench adapted for MicroBlaze baremetal
################################################################################

export KERNEL_OPTIONS=-DDATA_TYPE_IS_INT -DMINI_DATASET
export ROOT=$(shell pwd)
SETS=datamining linear-algebra medley stencils

.PHONY: all clean $(SETS)
all:  libs $(SETS)

libs:
	$(MAKE) -C ./lib/standalone_microblaze_0/bsp/ all

$(SETS):
	$(MAKE) -C ../polybenchKernels/$@ all

clean:
	for s in $ $(SETS) ; do \
		$(MAKE) -C ../polybenchKernels/$$s clean && wait ; \
	done
	$(MAKE) -C ./lib/standalone_microblaze_0/bsp/ clean
	-$(RM) ./build/*.elf

