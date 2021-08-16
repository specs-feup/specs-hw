################################################################################
# Makefile for Correlation
################################################################################

ifndef ROOT
$(error Root folder not defined!)
endif
export ROOT

ifndef KERNEL_OPTIONS
KERNEL_OPTIONS=-DDATA_TYPE_IS_FLOAT -DSMALL_DATASET
endif
KERNEL:=3mm
RELATIVE:=../../..
-include $(ROOT)/util/makefile.master

