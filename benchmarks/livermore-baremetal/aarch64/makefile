################################################################################
# Makefile for Livermore adapted for aarch64 baremetal
################################################################################

export ROOT=$(shell pwd)
KERNELS=CHOLESKY DIFFERENCE_PREDICT G_LINEAR_RECURRENCE HYDRO HYDRO_2D HYDRO_2D_IMPLICIT INNER_PROD INTEGRATE_PREDICT LIN_REC MATMUL PIC_1D PIC_2D STATE_FRAG TRI_DIAG

.PHONY: all clean $(SETS)
all:  libs $(KERNELS)

libs:
	$(MAKE) -C ./lib/standalone_bsp_0/ all

$(KERNELS):
	$(MAKE) -C ../livermoreKernels/$@ all

clean:
	for s in $ $(KERNELS) ; do \
		$(MAKE) -C ../livermoreKernels/$$s clean && wait ; \
	done
	$(MAKE) -C ./lib/standalone_bsp_0/ clean
	-$(RM) ./build/*.elf

