################################################################################
# Makefile for Polybench (kernels) adapted for MicroBlaze baremetal
################################################################################

ifndef ROOT
$(error Root folder not defined!)
endif
export ROOT

ifndef KERNEL_OPTIONS
export KERNEL_OPTIONS=-DDATA_TYPE_IS_INT -DSMALL_DATASET
endif

KERNELS=2mm 3mm atax bicg doitgen mvt

.PHONY: all clean $(KERNELS)
all: $(KERNELS)

$(KERNELS):
	$(MAKE) -C $@ all

clean:
	for l in $ $(KERNELS) ; do \
		$(MAKE) -C $$l clean && wait ; \
	done

