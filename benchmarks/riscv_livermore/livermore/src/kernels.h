
#ifndef _kernels_H_
#define _kernels_H_

#include "types.h"

typedef enum _test_type_idx {
	HYDRO_IDX = 0,				// 0x00000001
	CHOLESKY_IDX,				// 0x00000002
	INNER_PROD_IDX,				// 0x00000004
	G_LINEAR_RECURRENCE_IDX,	// 0x00000008
	STATE_FRAG_IDX,				// 0x00000010
	INTEGRATE_PREDICT_IDX,		// 0x00000020
	DIFFERENCE_PREDICT_IDX,		// 0x00000040
	PIC_2D_IDX,					// 0x00000080
	PIC_1D_IDX,					// 0x00000100
	HYDRO_2D_IDX,				// 0x00000200
	LIN_REC_IDX,				// 0x00000400
	MATMUL_IDX,					// 0x00000800
	HYDRO_2D_IMPLICIT_IDX,		// 0x00001000
	TRI_DIAG_IDX,				// 0x00002000
	LAST_TEST_IDX
} test_type_idx;

typedef enum test_type_e {
	HYDRO				= (e_u32)1<<HYDRO_IDX,
	CHOLESKY			= (e_u32)1<<CHOLESKY_IDX,
	INNER_PROD			= (e_u32)1<<INNER_PROD_IDX,
	G_LINEAR_RECURRENCE	= (e_u32)1<<G_LINEAR_RECURRENCE_IDX,
	STATE_FRAG			= (e_u32)1<<STATE_FRAG_IDX,
	INTEGRATE_PREDICT	= (e_u32)1<<INTEGRATE_PREDICT_IDX,
	DIFFERENCE_PREDICT	= (e_u32)1<<DIFFERENCE_PREDICT_IDX,
	PIC_2D				= (e_u32)1<<PIC_2D_IDX,
	PIC_1D				= (e_u32)1<<PIC_1D_IDX,
	HYDRO_2D			= (e_u32)1<<HYDRO_2D_IDX,
	LIN_REC				= (e_u32)1<<LIN_REC_IDX,
	MATMUL				= (e_u32)1<<MATMUL_IDX,
	HYDRO_2D_IMPLICIT	= (e_u32)1<<HYDRO_2D_IMPLICIT_IDX,
	TRI_DIAG			= (e_u32)1<<TRI_DIAG_IDX,
	LAST_TEST				
} test_type; 

extern char *kernelnames[LAST_TEST_IDX+1];
extern benchfunc_init kernelInit[LAST_TEST_IDX+1];
extern benchfunc kernels[LAST_TEST_IDX+1];
extern benchfunc_fini kernelFini[LAST_TEST_IDX+1];

#endif
