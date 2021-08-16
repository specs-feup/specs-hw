################################################################################
# Makefile for Polybench (stencils) adapted for MicroBlaze baremetal
################################################################################

ifndef ROOT
$(error Root folder not defined!)
endif
export ROOT

ifndef KERNEL_OPTIONS
export KERNEL_OPTIONS=-DDATA_TYPE_IS_INT -DSMALL_DATASET
endif

KERNELS=adi fdtd-2d heat-3d jacobi-1d jacobi-2d seidel-2d

.PHONY: all clean $(KERNELS)
all: $(KERNELS)

$(KERNELS):
	$(MAKE) -C $@ all

clean:
	for l in $ $(KERNELS) ; do \
		$(MAKE) -C $$l clean && wait ; \
	done

