################################################################################
# Makefile for Polybench adapted for MicroBlaze baremetal
################################################################################

export KERNEL_OPTIONS=-DDATA_TYPE_IS_FLOAT -DSMALL_DATASET

SETS=datamining linear-algebra medley stencils

.PHONY: all clean $(SETS)
all: $(SETS)

$(SETS):
	$(MAKE) -C $@ all

clean:
	for s in $ $(SETS) ; do \
		$(MAKE) -C $$s clean && wait ; \
	done

