#include <stdlib.h>
#include "types.h"
#include "rand.h"
#include "harness.h"

#define VERBOSE (0)

void mmemset(void *s, char c, size_t n) {

	char *ptr = (char *)(s);
	int i = 0;
	for(i = 0; i < n; i++, ptr++)
		*ptr = c;

	return;
}

void mmemcpy(void *d, void *s, size_t n) {

	char *ptr1 = (char *)(d);
	char *ptr2 = (char *)(s);
	int i = 0;
	for(i = 0; i < n; i++, ptr1++, ptr2++)
		*ptr1 = *ptr2;

	return;
}

void mexit(const char *msg, const char *file, int nr) {
	printFunc("error: %s:%d - %s\r\n", file, nr, msg);
#ifdef RVEX
	while(1) {
		printFunc("Terminated..\r");
	}
#else
	exit(1);
#endif	
}

////////////////////////////////////////////////////////////////////////////////
void verifyResults(test_params *p) {

	int t = 0;
	intparts *refdata;
	intparts num;	
	
	printFunc("Verifying results...\r\n");
	
	for(t = 0; t < p->maxnumkernels; t++) {

		// if test not to be called
		if((p->tests & ((int)1 << t)) == 0)
			continue;
	
		refdata = &(p->ref_data[t]);

		load_sp(&(p->v2[t]), &num);
			// turn data computed in this run into its intparts
		
		// printFunc("%d - %x - %x - %x\r\n", 
			// num.sign, num.exp, num.mant_high32, num.mant_low32);
			
		// printFunc("v2 %f\r\n", p->v2[t]); 
		
		// compare stuff	
		p->success[t] = 0;
		if(num.sign == refdata->sign && num.exp == refdata->exp && 
			num.mant_high32 == refdata->mant_high32 && num.mant_low32 == refdata->mant_low32)	
				p->success[t] = 1;
					// ISSUE: bit inaccuracies...
			
		p->run_data[t] = num;
	}

	return;
} 

#ifdef DESKTOP
static void printRandArraysToStatic(FILE *fd, test_params *params)
{
	int i = 0;

	fprintf(fd, "\ne_f32 static_rbank[] = {\n");
	for (i=0; i < RANDARRAYSIZE - 1; i++) {
		fprintf(fd, "%f, ", params->rbank[i]);	
	}
	fprintf(fd, "%f};\n\n", params->rbank[i]);
	
	fprintf(fd, "e_u32 static_irbank[] = {\n");
	for (i=0; i < RANDARRAYSIZE - 1; i++) {
		fprintf(fd, "0x%x, ", params->irbank[i]);	
	}
	fprintf(fd, "0x%x};\n\n", params->irbank[i]);
	
	return;
}

// called from desktop if gen_ref 1, generates outputs so that a board compiled
// version of this code has the reference.c file to hold the validation results
int makeReference(void *in_params) {

	int ret=1;
	int i;
	intparts num;
	
	test_params *params=(test_params *)in_params;

	FILE *fd = fopen("reference.c", "w");
	if(!fd)
		mexit("File error!", __FILE__,__LINE__ );	
	
	fprintf(fd, "/**** START DATASET ****/\n#include \"types.h\"\n#include \"harness.h\"\n");
	fprintf(fd, "static intparts ref_data_index[]={\n"); 
	for (i=0; i<params->maxnumkernels+1; i++) {
		fprintf(fd, "\t");
		
		load_sp(&(params->v2[i]), &num);
		
		fprintf(fd, "{%u,%d,0x%08x,0x%08x}/*%1.18e*/",
			num.sign,num.exp,num.mant_high32,num.mant_low32,params->v2[i]);
		
		if (i<params->maxnumkernels)
			fprintf(fd, ",\n");
	}
	fprintf(fd, "\n}; /* ref_data */\n\n"); 
	fprintf(fd, "void init_preset_0(test_params *params) {\n");
	fprintf(fd, "\tparams->maxnumkernels = %d;\n", params->maxnumkernels);
	fprintf(fd, "\tparams->seed=0x%x;\n",params->seed);
	fprintf(fd, "\tparams->tests=0x%08x;\n",params->tests);
	fprintf(fd, "\tparams->N=0x%x;\n",params->N);
	fprintf(fd, "\tparams->Loop=0x%x;\n",params->Loop);
	
	fprintf(fd, "\tmmemcpy(&(params->ref_data), "
	"&(ref_data_index), sizeof(intparts) * 32);\n}\n");

	if(params->storeToStatic)
		printRandArraysToStatic(fd, params);
	
	// fprintf(fd, "\tparams->ref_data=ref_data_index;\n}\n");
	
	fprintf(fd, "/**** END DATASET ****/\n");

	fclose(fd);
	return ret;
}
#endif

#if !defined(STATICMEMORY)
static int num_vectors=10;
static int num_2d_matrixes=10;
static int num_3d_matrixes=10;
#endif

static void initDataBanks(void *in_params) {

	test_params *params;
	if(!in_params)
		mexit("Invalid pointer", __FILE__,__LINE__ );

	/* Create a params for this invocation */
	params = (test_params *)in_params;
	
#if !defined(STATICMEMORY)
	// allocate arrays of pointers	
	params->v=(e_fp **)qmalloc(num_vectors, sizeof(e_fp *), __FILE__, __LINE__);
		// allocate array of pointers to *e_fp
	
	params->iv=(e_u32 **)qmalloc(num_vectors, sizeof(e_u32 *), __FILE__, __LINE__);
		// allocate array of pointers to *e_u32

	params->i16v=(e_u16 **)qmalloc(num_vectors, sizeof(e_u16 *), __FILE__, __LINE__);
		// allocate array of pointers to *e_u16

	params->i8v=(e_u8 **)qmalloc(num_vectors, sizeof(e_u8 *), __FILE__, __LINE__);
		// allocate array of pointers to *e_u16
		
	params->m2=(e_fp **)qmalloc(num_2d_matrixes, sizeof(e_fp *), __FILE__, __LINE__);
		// allocate array of pointers to *e_fp
		
	params->m3=(e_fp **)qmalloc(num_3d_matrixes, sizeof(e_fp *), __FILE__, __LINE__);
		// allocate array of pointers to *e_fp
#endif

	//mmemset((void *)(params->v2), 0, sizeof(e_fp) * (params->maxnumkernels));

	return;
}

static void *runtests(void *in_params) {

	test_params *params=(test_params *)in_params;
	e_fp retval = 0.0;
	
	benchfunc *kernels = params->kernels;
	benchfunc_init *kernels_init = params->kernels_init;
	benchfunc_fini *kernels_fini = params->kernels_fini;
		// pointers to functions given to harness

	int t = 0;
	for(t = 0; t < params->maxnumkernels; t++) {

		// if test not to be called
		if((params->tests & ((int)1 << t)) == 0)
			continue;

#if VERBOSE && SIM==0
		printFunc("Running %s\n\r", kernelNames[t]);
#endif
		kernels_init[t](params);
			
		////////////////////////////////////////////////////////////////////////
		// run the test
		retval = kernels[t](params);

		////////////////////////////////////////////////////////////////////////		
		
		kernels_fini[t](params);		
		
		// save single result
		params->v2[t]=retval;
		
		// accumulate results
		params->val += retval;
	}

	return (void*) params;
}

void freeDataBanks(void *in_params) {

	test_params *params=(test_params *)in_params;

#if !defined(STATICMEMORY)
	if(params->v)
		free(params->v);

	if(params->iv)
		free(params->iv);

	if(params->i16v)
		free(params->i16v);

	if(params->i8v)
		free(params->i8v);
		
	if(params->m2)
		free(params->m2);

	if(params->m3)
		free(params->m3);
		
	if (params->irbank)
		free(params->irbank);

	if (params->rbank)
		free(params->rbank);
#endif
	return;
}

#if defined(STATICMEMORY)	
extern e_f32 static_rbank[];
extern e_u32 static_irbank[];
#endif

// mostly generate random arrays and derivative parameters
static void initInnerParams(test_params *params) {

	int i;

#if !defined(STATICMEMORY)	
	void *r = rand_intparts_init(params->seed, 256, 1, 1, 10, 0x0, 0x000fffff);
	params->rbank = fromint_random_fp_vector(RANDARRAYSIZE, r);
	rand_fini(r);
	
	for (i=0; i<RANDARRAYSIZE; i++) {
		if (i&1) 
			params->rbank[i] -= 0.99;
		else 
			params->rbank[i] -= 1.01;
	}

	params->irbank = random_u32_vector(RANDARRAYSIZE, params->seed);
		// random array of integers

// use generated random numbers that are included with the dataset file
	// i.e. usage of STATICMEMORY for the harness implies a previous prior
	// run in the desktop platform that inits these arrays
		// this makes most of rand.c unecessary
#else
	params->rbank = static_rbank;
	params->irbank = static_irbank;
#endif
		
	params->vsize=params->N;
	params->ivsize=params->N;
	
	params->m2size=params->N*4+4;
	params->m2size+=params->Loop;

	params->m3size=params->N*8;	

	return;
}

////////////////////////////////////////////////////////////////////////////////
// visible to workload (i.e. "main" function)
// results (tmr counts etc get returned in params

#if !defined(STATICMEMORY)	
#define MEMCHECK
void* qmalloc(int nr, int size, char *file, int line)
{
	void *ptr = malloc(nr * size);
#ifdef MEMCHECK
	if(!ptr && nr > 0)
		mexit("Cannot Allocate Memory", file, line);
#endif
	return ptr;
}
#endif
	// should not exist if STATICMEMORY used.
	// do not define an #else on purpose to get intentional errors if
	// any uses of malloc are left

e_fp *reinit_vec(test_params *params, e_fp *p, int nvals) {
	int i=0;
	int si=random_u32(params->r);
	while (i < nvals) {
		p[i]=params->rbank[si++ & RANDARRAYMASK];
		i++;
	}
	return p;
}

void reinit_ivec_pos(test_params *params, e_u32 *p, int nvals, e_u32 mask) {
	int i=0;
	int si=random_u32(params->r);
	while (i<nvals) {
		p[i]=(params->irbank[si++ & RANDARRAYMASK] & mask) + 1;
		i++;
	}
}

void reinit_ivec(test_params *params, e_u32 *p, int nvals, e_u32 mask) {
	int i=0;
	int si=random_u32(params->r);
	while (i<nvals) {
		p[i]=params->irbank[si++ & RANDARRAYMASK] & mask;
		i++;
	}
}

void reinit_ivec_short(test_params *params, e_u16 *p, int nvals, e_u16 mask) {
	int i=0;
	int si=random_u32(params->r);
	while (i<nvals) {
		p[i]=(e_u16) (params->irbank[si++ & RANDARRAYMASK] & mask);
		i++;
	}
}

void reinit_ivec_char(test_params *params, e_u8 *p, int nvals, e_u8 mask) {
	int i=0;
	int si=random_u32(params->r);
	while (i<nvals) {
		p[i]=(e_u8) (params->irbank[si++ & RANDARRAYMASK] & mask);
		i++;
	}
}

void zero_vec( e_fp *p, int nvals)
{
	int i = 0;
	for (i=0 ; i<nvals; i++) 
		p[i] = 0.0;
		
	return;
}

#ifndef DESKTOP

void *initParameters_fromPreset(
	int maxnumkernels,
	char **kernelNames,
	benchfunc *kernels,
	benchfunc_init *kernels_init,
	benchfunc_fini *kernels_fini,
	test_params *preset
	) {

	preset->maxnumkernels = maxnumkernels;
	preset->kernelNames = kernelNames;
	preset->kernels = kernels;
	preset->kernels_init = kernels_init;
	preset->kernels_fini = kernels_fini;
	preset->verify = 1;
	
	initInnerParams(preset);
	return preset;
}

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
	) {

	// allocate parameters
	//test_params *params = 
		//(test_params *) qmalloc(1, sizeof(test_params), __FILE__, __LINE__);

	test_params sparams;	
	test_params *params = &sparams;	
	//mmemset((void *)params, 0, sizeof(test_params));
	
	// init from arguments given by user level
	{
		params->maxnumkernels = maxnumkernels;
		params->kernelNames = kernelNames;
		params->kernels = kernels;
		params->kernels_init = kernels_init;
		params->kernels_fini = kernels_fini;
		
		params->tests = tests;
		params->N = N;
		params->Loop = Loop;
		params->seed = seed;	//random nr
		params->gen_ref = gen_ref;
		params->verify = verify;
		params->storeToStatic = storeToStatic;
	}

	initInnerParams(params);

	return sparams;
	//return (void*) params;
}
#endif

void runAll(void *in_params) {

	test_params *params;

	initDataBanks(in_params);	
	runtests(in_params);
	freeDataBanks(in_params);
	
	params = (test_params *)in_params;
	
	if(params->verify && params->gen_ref == 0)
		verifyResults(params);

#ifdef DESKTOP	
	if(params->gen_ref)
		makeReference(params);
#endif
		
	return;
}

void printResults(void *in_params) {

	int i = 0;
	
	test_params *params=(test_params *)in_params;	

	printFunc("tests: %x - N=%d - l=%d\n\r", 
		params->tests, params->N, params->Loop);

	// print results	
	for(i = 0; i < params->maxnumkernels; i++) {
	
		// if test wasn't performed
		if((params->tests & ((int)1 << i)) == 0)
			continue;

		// runtime / valid result
		printFunc("%s:\r\n\t"
			"wrap: %d (%d)\r\n\t"
			"inj: %d (%d)\r\n\t"
			"rpu: %d (%d)\r\n\t"
			"iterations: %d\r\n\t"
			"rpu calls: %d\t inj calls: %d\r\n\t"
			"m1: %d - m2: %d\r\n\t"
			"result: %s - (ref=%d:%d:%x vs res=%d:%d:%x)\r\n\r\n", 
			params->kernelNames[i], 
			params->runtime[i], params->runtime_ovf[i], 
			params->injtime[i], params->injtime_ovf[i], 
			params->rputime[i], params->rputime_ovf[i], 
			params->iterations[i], 
			params->rpuenable[i],
			params->injenable[i],
			params->mem1[i], 
			params->mem2[i], 			
			((params->success[i] == 1) ? "sucess" : "failure"), 			
			refdata->sign, refdata->exp, refdata->mant_low32,
			rundata->sign, rundata->exp, rundata->mant_low32);
	}

	return;
}
