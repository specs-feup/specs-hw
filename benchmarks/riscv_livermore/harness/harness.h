
#ifndef _harness_H_
#define _harness_H_

#include "types.h"   

struct test_params_s {

	// benchmark functions
	e_u32 maxnumkernels;
	char **kernelNames;
	benchfunc *kernels;
	benchfunc_init *kernels_init;
	benchfunc_fini *kernels_fini;
		// expects a maximum of 32 kernels

	/* Benchmark setup */
	e_u32 tests;		//Type of tests to perform. Will be performed in enum order.
	e_u32 vsize;		//Data size to use for arrays
	e_u32 ivsize;
	e_u32 m2size;		//Data size to use for matrixes
	e_u32 m3size;		//Data size to use for 3D matrixes
	
	// Elements to process at each kernel
	e_u32 N;
		// N is (may be) locally recomputed at kernel init function level
		// this function init is not accounted in measurements
	e_u32 Nact;
		// recalculation of N for the benchmark being called;
	
	// Times to repeat kernel
	e_u32 Loop;
	
	e_u32 seed;				// seed for random number generator
	e_u32 gen_ref;			// save output data if gen_ref = 1
 	e_u32 storeToStatic;	// if gen_ref = 1 and this = 1, then the desktop harness 
							// saves, along with the run results, the rbank, irbank, and 
							// arrays allocated during the kernel run
	e_u32 verify;

	// heap data:

		void *r; // pointer of random number generator
		e_fp *rbank;
		e_u32 *irbank;
			// alloc these 2 arrays once based on "r" generator

#if !defined(STATICMEMORY)
		// user may alloc/free these arrays during kernels_init and kernels_fini
		e_fp **v;
		e_u32 **iv;
		e_u16 **i16v;
		e_u8 **i8v;
		e_fp v2[32];
		e_fp **m2;
		e_fp **m3;
#else
		e_fp *v[10];
		e_u32 *iv[10];
		e_u16 *i16v[10];
		e_u8 *i8v[10];
		e_fp v2[32];
		e_fp *m2[10];
		e_fp *m3[10];
#endif	
	
	/* Verification and return values */
	e_u32 runtime[32];
	e_u32 runtime_ovf[32];
		// runtime per kernel for all "Loop" repetitions (i.e. e_s32 Loop)
	
	e_u32 injtime[32];
	e_u32 injtime_ovf[32];
		// time measured only by injector (around trace)

	e_u32 rputime[32];
	e_u32 rputime_ovf[32];
		
	e_u32 injenable[32];
	e_u32 rpuenable[32];
		// tmr enable counts
		
	e_u32 iterations[32];
		// iterations performed
	
	e_u32 mem1[32], mem2[32];
		// memory port usage
	
	e_u32 success[32];
		// 1 if results check out with reference data
	
	e_fp val;
	intparts ref_data[32];
		// reference data (float) stored as integer parts
		// allows for storing of both integer and float result data for 
		// comparison routines that take into account bit precision

	intparts run_data[32];
};

void mmemset(void *s, char c, size_t n);
void mmemcpy(void *d, void *s, size_t n);
void mexit(const char *msg, const char *file, int nr);
e_fp *reinit_vec(test_params *params, e_fp *p, int nvals);
void reinit_ivec_pos(test_params *params, e_u32 *p, int nvals, e_u32 mask);
void reinit_ivec(test_params *params, e_u32 *p, int nvals, e_u32 mask);
void reinit_ivec_short(test_params *params, e_u16 *p, int nvals, e_u16 mask);
void reinit_ivec_char(test_params *params, e_u8 *p, int nvals, e_u8 mask);
void zero_vec( e_fp *p, int nvals);
void* qmalloc(int nr, int size, char *file, int line);

#ifndef DESKTOP
void *initParameters_fromPreset(
	int maxnumkernels,
	char **kernelNames,
	benchfunc *kernels,
	benchfunc_init *kernels_init,
	benchfunc_fini *kernels_fini,
	test_params *preset
	);

#else
test_params initParameters_fromWorkLoad(
int maxnumkernels,
	char **kernelNames,
	benchfunc *kernels,
	benchfunc_init *kernels_init,
	benchfunc_fini *kernels_fini,
	e_u32 tests,
	e_u32 N,
	e_u32 Loop,
	e_u32 seed,
	e_u32 gen_ref,
	e_u32 verify,
	e_u32 storeToStatic
	);
#endif

void runAll(void *params);
void printResults(void *in_params);

#ifdef DESKTOP
int makeReference(void *in_params);
#endif

#endif
