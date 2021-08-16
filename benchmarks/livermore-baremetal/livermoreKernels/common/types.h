
#ifndef _types_H_
#define _types_H_

#include <stddef.h>

#ifdef DESKTOP
	#include <stdio.h>
	#include <time.h>
	#include <stdlib.h>
	#include <unistd.h>
	#define printFunc printf
#else
/*#ifndef RVEX
	#include "xparameters.h"
	#include "xbasic_types.h"
	#define printFunc xil_printf
#else*/
	#define printFunc(ARGS...) 	// removes all prints
//#endif
#endif

typedef unsigned char e_u8;
typedef char e_s8;
typedef unsigned short e_u16;
typedef short e_s16;
typedef unsigned int e_u32;

	typedef unsigned long long e_u64;

typedef int e_s32;
typedef float e_f32;
typedef double e_f64;

// if 32 bit calculations
typedef e_f32 e_fp;

typedef struct intparts_s {
	e_s8 sign;
	e_s16 exp;
	e_u32 mant_high32;
	e_u32 mant_low32;
} intparts;

struct test_params_s;
typedef struct test_params_s test_params;

typedef void (*benchfunc_init)(test_params *par);
typedef e_fp (*benchfunc)(test_params *par);
typedef void (*benchfunc_fini)(test_params *par);

#endif
