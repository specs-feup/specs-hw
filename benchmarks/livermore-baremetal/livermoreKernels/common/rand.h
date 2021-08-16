
#ifndef _RAND_H_
#define _RAND_H_

#include "types.h"

void *rand_intparts_init(e_u32 seed, e_u32 reseed, 	
	e_s32 mantsign,
	e_s32 expsign,
	e_u32 exp_cut,
	e_u32 manthigh_cut,
	e_u32 mantlow_cut);

#define fromint_random_fp_vector fromint_random_f32_vector
int store_sp(e_f32 *value, intparts *asint);
int load_sp(e_f32 *value, intparts *asint);
e_u32 random_u32(void *pr);

#if !defined(STATICMEMORY)
// Call this function to destroy a random number generator.
void rand_fini(void *r);

// Call this function to get a 32b random integer.
e_u32 *random_u32_vector(e_u32 size, e_u32 seed);

// Create a vector with randoms based on RNG that was previously allocated for intparts
e_f32 *fromint_random_f32_vector(int N,void *pr);

#else

void random_u32_vector(e_u32 size, e_u32 seed, e_u32 *ptr);
void fromint_random_f32_vector(int N,void *pr, e_f32 *ptr);
#endif

typedef enum SIGNRAND_E
{
	RAND=0,
	POS=1,
	NEG=2
} SIGNRAND;

e_fp th_exp2(e_fp x);
e_fp complex_rand_fp(
	SIGNRAND sign_rand,
	e_s32 min_exp,
	e_s32 max_exp,
	e_u32 meaningfull_bits,
	void *pr);
	
/* A union which permits us to convert between a float and a 32 bit
   int.  */

typedef union
{
  float value;
  e_u32 word;
} ieee_float_shape_type;

/* Get a 32 bit int from a float.  */

#define GET_FLOAT_WORD(i,d)					\
do {								\
  ieee_float_shape_type gf_u;					\
  gf_u.value = (d);						\
  (i) = gf_u.word;						\
} while (0)

/* Set a float from a 32 bit int.  */

#define SET_FLOAT_WORD(d,i)					\
do {								\
  ieee_float_shape_type sf_u;					\
  sf_u.word = (i);						\
  (d) = sf_u.value;						\
} while (0)	

#if SIM==0
	#define RANDARRAYSIZE 0x20
	#define RANDARRAYMASK 0x1F
#else
	#define RANDARRAYSIZE 0x20
	#define RANDARRAYMASK 0x1F
#endif

#endif //_TH_RAND_H_
