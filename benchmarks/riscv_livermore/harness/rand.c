#include <stdlib.h>
#include "types.h"
#include "rand.h"

/* no denormal/inf/nan support */
int store_sp(e_f32 *value, intparts *asint)
{
	e_u32 iValue;
	e_f32 v32;
	e_u32 iexp;
	e_s32 exp=asint->exp;
	e_u32 mant=asint->mant_low32;

	if (asint->mant_high32) {
		return 0;
	}

	if (mant >= ((e_u32)1<<24)) {
		return 0;
	}
	
	if (!(mant & ((e_u32)1<<23))) {
	
		/* special casing signed zero */
		if (exp == 0 && mant == 0) {
			iValue = (e_u32)(asint->sign) << 31;
			SET_FLOAT_WORD(v32, iValue);
			*value = v32;
			return 1;
		}
		return 0;
	}

	mant &= ((e_u32)1 << 23) - 1;

	exp += 127;
	if (exp <= 0 || exp >= 255) {
		return 0;
	}
	
	iexp = exp << 23;
	if (asint->sign) {
		iexp |= 0x80000000;
	}
	iValue = mant | iexp;    

	SET_FLOAT_WORD(v32, iValue);
	*value = v32;
	return 1;
}

int load_sp(e_f32 *value, intparts *asint)
{
   e_u32 iValue;

   if (!value || !asint)
      return 0;

   GET_FLOAT_WORD(iValue, *value);
   
   asint->sign = 0;
   asint->mant_high32 = 0;
   asint->mant_low32 = (iValue & (((e_u32)1 << 23) - 1));
   asint->exp = ((iValue >> 23) & 255);
   
   if (asint->exp == 255)
      return 0;

   if (asint->exp != 0)
   {
      asint->mant_low32 |=  ((e_u32)1 << 23);
      asint->exp -= 127;
   }
   else
   {
      if (asint->mant_low32)
         return 0;
   }
   asint->sign = iValue >> 31;
   return 1;
}

typedef struct rand_state_s {
	e_u32 idx;
	e_u32 rsl[256];
    e_u32 mm[256];
    e_u32 aa, bb, cc;
	e_u32 initial_seed;
	e_u32 reseed;
	e_fp range;
	e_fp min;
	e_s32 mantsign; // negative mantsign, force negative. zero, force positive. +1, random. 
	e_s32 expsign;  // negative expsign, force negative. zero, force positive, +1, random.
	e_u16 exp_cut;
	e_u32 manthigh_cut;
	e_u32 mantlow_cut;

} rand_state;

static void isaac(void *pr)
{
   register e_u32 i,x,y;
   rand_state *r=(rand_state *)pr;
   e_u32 *mm=r->mm;
   e_u32 *randrsl=r->rsl;
   e_u32 aa=r->aa, bb=r->bb, cc=r->cc;

   cc = cc + 1;    /* cc just gets incremented once per reseed results */
   bb = bb + cc;   /* then combined with bb */

   for (i=0; i<256; ++i)
   {
     x = mm[i];
     switch (i%4)
     {
     case 0: aa = aa^(aa<<13); break;
     case 1: aa = aa^(aa>>6); break;
     case 2: aa = aa^(aa<<2); break;
     case 3: aa = aa^(aa>>16); break;
     }
     aa              = mm[(i+128)%256] + aa;
     mm[i]      = y  = mm[(x>>2)%256] + aa + bb;
     randrsl[i] = bb = mm[(y>>10)%256] + x;
   }
   r->aa=aa;r->bb=bb;r->cc=cc;
}

#define mix(a,b,c,d,e,f,g,h) \
{ \
   a^=b<<11; d+=a; b+=c; \
   b^=c>>2;  e+=b; c+=d; \
   c^=d<<8;  f+=c; d+=e; \
   d^=e>>16; g+=d; e+=f; \
   e^=f<<10; h+=e; f+=g; \
   f^=g>>4;  a+=f; g+=h; \
   g^=h<<8;  b+=g; h+=a; \
   h^=a>>9;  c+=h; a+=b; \
}

e_u32 random_u32(void *pr) {
	rand_state *r=(rand_state *)pr;
	if (r==NULL)
		return 0;
	r->idx++;
	if (r->idx > r->reseed) { isaac(r); r->idx=0; }
	return r->rsl[r->idx&0xff];
}

e_u16 random_u16(void *pr) {
	rand_state *r=(rand_state *)pr;
	return ((e_u16)random_u32(r));
}

e_f32 precise_random_f32(void *pr) {
	rand_state *r=(rand_state *)pr;
	e_f32 res;
	intparts num;
	num.sign=r->mantsign < 0 ?  1 : (r->mantsign  & (random_u32(pr)&1))==0 ? 0  :  1;  // negative mantsign, force negative. zero, force positive. +1, random. 
	num.exp= r->expsign  < 0 ? -1 : (r->expsign   & (random_u32(pr)&1))==0 ? 1  : -1;  // negative expsign, force negative. zero, force positive, +1, random.
	num.mant_low32=random_u32(pr);	
	num.exp *= 1 + (random_u16(pr) & (r->exp_cut));
	num.mant_low32 &= r->mantlow_cut;
	num.mant_low32 |= (((e_u32)1)<<23); 
	num.mant_high32=0;
	store_sp(&res,&num);
	return res;
}

#if !defined(STATICMEMORY)
void rand_fini(void *r) {
	free(r);
}
#else
rand_state staticr;
#endif //#if !defined(STATICMEMORY)	

void *rand_init(e_u32 seed, e_u32 reseed, e_f64 min, e_f64 max)
{
	int i;
	e_u32 a,b,c,d,e,f,g,h;

#if !defined(STATICMEMORY)
	rand_state *r=(rand_state *)malloc(sizeof(rand_state));
#else
	rand_state *r=&staticr;
#endif

	e_u32 *mm=r->mm;

	r->reseed=0xff*reseed;
	r->range=max-min;
	r->min=min;
	if (r->reseed==0) r->reseed=0xffffffff;
	r->aa=r->bb=r->cc=0;
	r->initial_seed=a=b=c=d=e=f=g=h=seed;  
	r->idx=0;

	// scramble the seed
	for (i=0; i<4; ++i){
		mix(a,b,c,d,e,f,g,h);
	}
	
	// fill in mm[] with messy stuff
	for (i=0; i<256; i+=8) {
		mix(a,b,c,d,e,f,g,h);
		mm[i  ]=a; mm[i+1]=b; mm[i+2]=c; mm[i+3]=d;
		mm[i+4]=e; mm[i+5]=f; mm[i+6]=g; mm[i+7]=h;
	}

	isaac(r);            /* fill in the first set of results */
	return (void*) r;
}

#if !defined(STATICMEMORY)
void *rand_intparts_init(e_u32 seed, e_u32 reseed, 	
	e_s32 mantsign,
	e_s32 expsign,
	e_u32 exp_cut,
	e_u32 manthigh_cut,
	e_u32 mantlow_cut) {
	rand_state *r=(rand_state *)rand_init(seed,reseed,0.0,0.0);
	r->mantsign=mantsign;
	r->expsign=expsign;
	r->exp_cut=(e_u16)exp_cut;
	r->manthigh_cut=manthigh_cut;
	r->mantlow_cut=mantlow_cut;
	return (void*) r;
}

e_u32 *random_u32_vector(e_u32 size, e_u32 seed) {
	void *r=rand_init(seed,256,-1e10,1e10);
	e_u32 N=size;
	e_u32 *p=(e_u32 *)malloc(sizeof(e_u32)*N);
	/*while (N-->0) {
		p[N]=random_u32(r);
	}*/
	rand_fini(r);
	return p;
}

e_f32 *fromint_random_f32_vector(int N,void *pr) {
	e_f32 *v=(e_f32 *)malloc(sizeof(e_f32)*N);
	/*int i=0;
	for (i=0 ; i<N ;i++)
		v[i]=precise_random_f32(pr);*/
	return v;
}

// version of random_u32_vector and fromint_random_f32_vector 
// that init an array already allocated
#else

void random_u32_vector(e_u32 size, e_u32 seed, e_u32 *ptr) {

	void *r=rand_init(seed,256,-1e10,1e10);
	e_u32 N=size;

	while (N-->0)
		ptr[N]=random_u32(r);

	return;
}

void fromint_random_f32_vector(int N,void *pr, e_f32 *ptr) {

	int i=0;
	for (i=0 ; i<N ;i++)
		ptr[i]=precise_random_f32(pr);

	return;
}

#endif //#if !defined(STATICMEMORY)	
