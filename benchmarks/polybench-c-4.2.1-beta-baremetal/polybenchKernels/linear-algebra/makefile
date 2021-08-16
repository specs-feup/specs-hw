################################################################################
# Makefile for Polybench (linear-algebra) adapted for MicroBlaze baremetal
################################################################################

ifndef KERNEL_OPTIONS
export KERNEL_OPTIONS=-DDATA_TYPE_IS_INT -DSMALL_DATASET
endif

SETS=blas kernels solvers

.PHONY: all clean $(SETS)
all: $(SETS)

$(SETS):
	$(MAKE) -C $@ all

clean:
	for s in $ $(SETS) ; do \
		$(MAKE) -C $$s clean && wait ; \
	done

