################################################################################
# Makefile for Correlation
################################################################################

ifndef ROOT
$(error Root folder not defined!)
endif
export ROOT

ifndef KERNEL_OPTIONS
KERNEL_OPTIONS=-DDATA_TYPE_IS_INT -DSMALL_DATASET
endif
KERNEL:=floyd-warshall
RELATIVE:=../..
-include $(ROOT)/util/makefile.master

