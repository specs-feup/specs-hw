#include <stdlib.h>
#include "kernels.h"
#include "types.h"
#include "harness.h"

static e_fp get_array_feedback(e_fp *a, int maxidx) {
	int i;
	e_fp ret=0.0;
	if (maxidx==0) return 0.0;
	for (i=0; i<maxidx; i++) {
		if (i&1) ret+=a[i];
		else ret-=a[i];
	}

	return ret/(e_fp)maxidx;
}

/******************************************************************************/
/*   Kernel 1 -- hydro fragment												  */
/******************************************************************************/
void hydro_fragment_init(test_params *p) {

	p->Nact = p->N;
	if (p->Nact > 100)
		p->Nact -= 99;

	// allocate 3 vectors in vector array "v" with Nact elements
	int i = 0;
	for (i = 0; i < 3; i++) {
		p->v[i]=(e_fp *) 
			qmalloc((p->Nact + 11 + 10), sizeof(e_fp), __FILE__,__LINE__);
	}	
	
	// and 3 elements in array v[3]
	p->v[3]=(e_fp *) qmalloc(4, sizeof(e_fp), __FILE__,__LINE__ );
	
	reinit_vec(p, p->v[0], p->Nact + 11);
	reinit_vec(p, p->v[1], p->Nact + 11);
	reinit_vec(p, p->v[2], p->Nact + 11);	
	reinit_vec(p, p->v[3], 4);

	return;
}

e_fp hydro_fragment(test_params *p) {

	e_fp *x=p->v[0], *y=p->v[1], *z=p->v[2];
	e_u32 n=p->Nact, loop=p->Loop,k,l;
	e_fp ret= 0.0;
	e_fp q=p->v[3][0];
	e_fp r=p->v[3][1];
	e_fp t=p->v[3][2];
	
    /*
     *******************************************************************
     *   Kernel 1 -- hydro fragment
     *******************************************************************
     *       DO 1 L = 1,Loop
     *       DO 1 k = 1,n
     *  1       X(k)= Q + Y(k)*(R*ZX(k+10) + T*ZX(k+11))
     */
	 
    for ( l=1 ; l<=loop ; l++ ) {
        for ( k=0 ; k<n ; k++ ) { 
            x[k] = q + y[k]*( r*z[k+10] + t*z[k+11] );
        }
    }
	ret += get_array_feedback(x,n);
	
	return ret;
}

void hydro_fragment_fini(test_params *p) {

	int i = 0;
	for (i = 0; i < 4; i++)
		free(p->v[i]);	
		// free working data

	return;
}

/******************************************************************************/
/*    Kernel 2 -- ICCG excerpt (Incomplete Cholesky Conjugate Gradient)		  */
/******************************************************************************/
void cholesky_init(test_params *p) {

	p->Nact = p->N;
	
	// allocate 2 vectors in vector array "v"
	int i = 0;
	for (i = 0; i < 2; i++)
		p->v[i]= (e_fp *) 
			qmalloc((p->Nact+10), sizeof(e_fp), __FILE__,__LINE__ );
		
	reinit_vec(p, p->v[0], p->N);
	reinit_vec(p, p->v[1], p->N);
		// fill vector v[0] and v[1] with random data
	return;
}

e_fp cholesky(test_params *p) {

	e_fp *x=p->v[0], *v=p->v[1];
	e_u32 n=p->Nact, loop=p->Loop;
	e_u32 ipntp, ipnt,ii,l,k,i;
	e_fp ret = 0.0;
	
    /*
     *******************************************************************
     *   Kernel 2 -- ICCG excerpt (Incomplete Cholesky Conjugate Gradient)
     *******************************************************************
     *    DO 200  L= 1,Loop
     *        II= n
     *     IPNTP= 0
     *222   IPNT= IPNTP
     *     IPNTP= IPNTP+II
     *        II= II/2
     *         i= IPNTP
     CDIR$ IVDEP
     *    DO 2 k= IPNT+2,IPNTP,2
     *         i= i+1
     *  2   X(i)= X(k) - V(k)*X(k-1) - V(k+1)*X(k+1)
     *        IF( II.GT.1) GO TO 222
     *200 CONTINUE
     */

    for ( l=1 ; l<=loop ; l++ ) {
        ii = n/2;
        ipntp = 0;
        do {
            ipnt = ipntp;
            ipntp += ii;
            ii /= 2;
            i = ipntp - 1;
            for ( k=ipnt+1 ; k<ipntp ; k=k+2 ) {
                i++;
                x[i] = x[k] - v[k  ]*x[k-1] - v[k+1]*x[k+1];
            }
        } while ( ii>0 );	
    }
	ret=get_array_feedback(x,n);

	return ret;
}

void cholesky_fini(test_params *p) {

	int i = 0;
	for (i = 0; i < 2; i++)
		free(p->v[i]);	
		// free working data

	return;
}

/******************************************************************************/
/*   Kernel 3 -- inner product										 		  */
/******************************************************************************/
void inner_product_init(test_params *p) {

	p->Nact = p->N;
	
	// allocate 2 vectors in vector array "v"
	p->v[0]=(e_fp *) qmalloc((p->N), sizeof(e_fp), __FILE__,__LINE__ );
	reinit_vec(p, p->v[0], p->N);
		
	p->v[1]=(e_fp *) qmalloc((p->N + p->Loop), sizeof(e_fp), __FILE__,__LINE__ );
	reinit_vec(p, p->v[1], (p->N + p->Loop));
		// fill vector v[0] and v[1] with random data
	
	return;
}

e_fp inner_product(test_params *p) {

	e_fp *x=p->v[0],  *z=p->v[1];
	int n=p->Nact, loop=p->Loop;
	int l,k;	
    e_fp q = 0.0;
	
	//reinit_vec_limited(p,x,p->vsize);
	//reinit_vec_limited(p,z,p->vsize);
		// NOTE: must be limited??
	
    /*
     *******************************************************************
     *   Kernel 3 -- inner product
     *******************************************************************
     *    DO 3 L= 1,Loop
     *         Q= 0.0
     *    DO 3 k= 1,n
     *  3      Q= Q + Z(k)*X(k)
	 *
	 *	SG : original code only had to perform one loop. Moved q initialization out of loop.
     */

    for ( l=1 ; l<=loop ; l++ ) {
        for ( k=0 ; k<n ; k++ ) {
            q += z[k+l]*x[k];
        }
    }
	return q;
}

void inner_product_fini(test_params *p) {

	int i = 0;
	for (i = 0; i < 2; i++)
		free(p->v[i]);	
		// free working data

	return;
}

/******************************************************************************/
/*   Kernel 6 -- general linear recurrence equations						  */
/******************************************************************************/

static float mfabs(float n) {
  float f;

  if (n >= 0) f = n;
  else f = -n;
  return f;
}

static float msqrt(float val) {
  float x = val/10.0;
  float dx;
  float diff;
  float min_tol = 0.00001;

  int i, flag;

	flag = 0;
	if (val == 0 ) x = 0;
	else {
		for (i=1;i<20;i++)
		{
			if (!flag) {
				dx = (val - (x*x)) / (2.0 * x);
				x = x + dx;
				diff = val - (x*x);
				if (mfabs(diff) <= min_tol) flag = 1;
			}
			else 
			x =x;
		}
	}
	return (x);
}

void linear_recurrence_init(test_params *p) {

	// int m2=(int)th_sqrt((e_fp)(params->N*4));
	// params->nsize[LINEAR_RECURRENCE_IDX]=m2;

	p->Nact = (int)msqrt((e_fp)(p->N*4));

	p->v[0]=(e_fp *) qmalloc(p->Nact, sizeof(e_fp), __FILE__,__LINE__ );
	reinit_vec(p, p->v[0], p->Nact);
		
	p->m2[0]=(e_fp *) qmalloc((p->Nact * p->Nact), sizeof(e_fp), __FILE__,__LINE__ );
	reinit_vec(p, p->m2[0], (p->Nact * p->Nact));

	return;
}

e_fp linear_recurrence(test_params *p) {

	e_fp *w=p->v[0],  *b=p->m2[0];
	int n=p->Nact, loop=p->Loop;
	int i,l,k=0;
	e_fp ret;

	reinit_vec(p,b,n*n);

    /*
     *******************************************************************
     *   Kernel 6 -- general linear recurrence equations
     *******************************************************************
     *    DO  6  L= 1,Loop
     *    DO  6  i= 2,n
     *    DO  6  k= 1,i-1
     *        W(i)= W(i)  + B(i,k) * W(i-k)
     *  6 CONTINUE
     */

    for ( l=1 ; l<=loop ; l++ ) {
        for ( i=1 ; i<n ; i++ ) {
            for ( k=0 ; k<i ; k++ ) {
                w[i] = b[k*n+i] * w[(i-k)-1];
            }
        }
    }
	ret=get_array_feedback(w,n);

	return ret;
}

void linear_recurrence_fini(test_params *p) {

	free(p->v[0]);
	free(p->m2[0]);
		// free working data

	return;
}

/******************************************************************************/
/*   Kernel 7 -- equation of state fragment									  */
/******************************************************************************/
void state_fragment_init(test_params *p) {

	p->Nact = p->N;
	
	// allocate 2 vectors in vector array "v"
	int i = 0;
	for (i = 0; i < 4; i++)
		p->v[i] = (e_fp *) 
			qmalloc((p->Nact+10), sizeof(e_fp), __FILE__,__LINE__);
		
	reinit_vec(p, p->v[0], p->Nact+10);
	reinit_vec(p, p->v[1], p->Nact+10);
	reinit_vec(p, p->v[2], p->Nact+10);
		// fill vector v[0] and v[1] and v[2] with random data

	return;
}

e_fp state_fragment(test_params *p) {

	e_fp *u=p->v[0],  *y=p->v[1],  *z=p->v[2];
	e_fp *x=p->v[3];
	int n=p->Nact, loop=p->Loop;
	int k,l;
	e_fp  r= 0.3;
	e_fp t = 0.25;
	e_fp ret = 0.0;

    /*
     *******************************************************************
     *   Kernel 7 -- equation of state fragment
     *******************************************************************
     *    DO 7 L= 1,Loop
     *    DO 7 k= 1,n
     *      X(k)=     U(k  ) + R*( Z(k  ) + R*Y(k  )) +
     *   .        T*( U(k+3) + R*( U(k+2) + R*U(k+1)) +
     *   .        T*( U(k+6) + R*( U(k+5) + R*U(k+4))))
     *  7 CONTINUE	 
     */
    for ( l=1 ; l<=loop ; l++ ) {
        for ( k=0 ; k<n ; k++ ) {
            x[k] = u[k] + r*( z[k] + r*y[k] ) +
                   t*( u[k+3] + r*( u[k+2] + r*u[k+1] ) +
                      t*( u[k+6] + r*( u[k+5] + r*u[k+4] ) ) );
        }
    }
	ret+=get_array_feedback(x,n);

	return ret;
}

void state_fragment_fini(test_params *p) {

	int i = 0;
	for (i = 0; i < 4; i++)
		free(p->v[i]);	
		// free working data

	return;
}

/******************************************************************************/
/*   Kernel 9 -- integrate predictors										  */
/******************************************************************************/

void integrate_predictors_init(test_params *p) {

	// params->nsize[INTEGRATE_PREDICTORS_IDX]=params->N/13;

	p->Nact = p->N/13;
	
	// allocate 1 vectors in vector array "v"
	p->v[0] = (e_fp *) qmalloc(30, sizeof(e_fp), __FILE__,__LINE__);
	reinit_vec(p, p->v[0], 30);
		// fill vector v[0] with random data

	p->m3[0]=(e_fp *) qmalloc(p->N, sizeof(e_fp), __FILE__,__LINE__);
	reinit_vec(p, p->m3[0], p->N);
	return;
}

e_fp integrate_predictors(test_params *p) {
	e_fp *dm=p->v[0];
	e_fp *px=p->m3[0];
	int n=p->Nact, loop=p->Loop;
	int l,i;
	e_fp ret= 0.0;
	
	reinit_vec(p,dm,30);
	reinit_vec(p,px,13*n);
	
    
     // *******************************************************************
     // *   Kernel 9 -- integrate predictors
     // *******************************************************************
     // *    DO 9  L = 1,Loop
     // *    DO 9  i = 1,n
     // *    PX( 1,i)= DM28*PX(13,i) + DM27*PX(12,i) + DM26*PX(11,i) +
     // *   .          DM25*PX(10,i) + DM24*PX( 9,i) + DM23*PX( 8,i) +
     // *   .          DM22*PX( 7,i) +  C0*(PX( 5,i) +      PX( 6,i))+ PX( 3,i)
     // *  9 CONTINUE
    

    for ( l=1 ; l<=loop ; l++ ) {		
        for ( i=0 ; i<n ; i++ ) {
            px[i*13] = dm[28]*px[i*13+12] + dm[27]*px[i*13+11] + dm[26]*px[i*13+10] +
                       dm[25]*px[i*13+ 9] + dm[24]*px[i*13+ 8] + dm[23]*px[i*13+ 7] +
                       dm[22]*px[i*13+ 6] + dm[0]*( px[i*13+ 4] + px[i*13+ 5]) + px[i*13+ 2];
        }
    }
	
	for( i=0 ; i<n ; i++ ) 
		ret+=px[i*13];

	return ret;
}

void integrate_predictors_fini(test_params *p) {
	
	free(p->v[0]);
	free(p->m3[0]);
		// free working data

	return;
}

/******************************************************************************/
/*   Kernel 10 -- difference predictors										  */
/******************************************************************************/

void difference_predictors_init(test_params *p) {

	// params->nsize[DIFFERENCE_PREDICTORS_IDX]=params->N/14;
	p->Nact = p->N/14;
	
	// allocate 1 vectors in vector array "v"
	p->v[0] = (e_fp *) qmalloc(p->Nact, sizeof(e_fp), __FILE__,__LINE__);
	reinit_vec(p, p->v[0], p->Nact);
		// fill vector v[0] with random data

	p->m3[0]=(e_fp *) qmalloc(p->N, sizeof(e_fp), __FILE__,__LINE__);
	reinit_vec(p, p->m3[0], p->N);
	return;
}

e_fp difference_predictors(test_params *p) {

	e_fp ar,br,cr;
	e_fp *px=p->m3[0];
	e_fp *cx=p->v[0];
	int n=p->Nact, loop=p->Loop;
	int i,l;
	
	// reinit_vec(p,px,13*n);
	// reinit_vec(p,cx,n);
	
    /*
     *******************************************************************
     *   Kernel 10 -- difference predictors
     *******************************************************************
     *    DO 10  L= 1,Loop
     *    DO 10  i= 1,n
     *    AR      =      CX(5,i)
     *    BR      = AR - PX(5,i)
     *    PX(5,i) = AR
     *    CR      = BR - PX(6,i)
     *    PX(6,i) = BR
     *    AR      = CR - PX(7,i)
     *    PX(7,i) = CR
     *    BR      = AR - PX(8,i)
     *    PX(8,i) = AR
     *    CR      = BR - PX(9,i)
     *    PX(9,i) = BR
     *    AR      = CR - PX(10,i)
     *    PX(10,i)= CR
     *    BR      = AR - PX(11,i)
     *    PX(11,i)= AR
     *    CR      = BR - PX(12,i)
     *    PX(12,i)= BR
     *    PX(14,i)= CR - PX(13,i)
     *    PX(13,i)= CR
     * 10 CONTINUE
     */

    for ( l=1 ; l<=loop ; l++ ) {
        for ( i=0 ; i<n ; i++ ) {
            ar        =      cx[i];
            br        = ar - px[14*i+ 4];
            px[14*i+ 4] = ar;
            cr        = br - px[14*i+ 5];
            px[14*i+ 5] = br;
            ar        = cr - px[14*i+ 6];
            px[14*i+ 6] = cr;
            br        = ar - px[14*i+ 7];
            px[14*i+ 7] = ar;
            cr        = br - px[14*i+ 8];
            px[14*i+ 8] = br;
            ar        = cr - px[14*i+ 9];
            px[14*i+ 9] = cr;
            br        = ar - px[14*i+10];
            px[14*i+10] = ar;
            cr        = br - px[14*i+11];
            px[14*i+11] = br;
            px[14*i+13] = cr - px[14*i+12];
            px[14*i+12] = cr;
        }
    }
	return px[0];
}

void difference_predictors_fini(test_params *p) {
	
	free(p->v[0]);
	free(p->m3[0]);
		// free working data

	return;
}

/******************************************************************************/
/*   Kernel 13 -- 2-D PIC (Particle In Cell)								  */
/******************************************************************************/
void pic_2d_init(test_params *p) {

	p->Nact = p->N;
	
	// allocate 2 vectors in vector array "v"
	p->v[0] = (e_fp *) qmalloc(100, sizeof(e_fp), __FILE__,__LINE__);
	p->v[1] = (e_fp *) qmalloc(100, sizeof(e_fp), __FILE__,__LINE__);
	reinit_vec(p,p->v[0],100);
	reinit_vec(p,p->v[1],100);
	
	// allocate 2 vectors in vector array "iv"
	p->iv[0] = (e_u32 *) qmalloc(100, sizeof(e_u32), __FILE__,__LINE__);
	p->iv[1] = (e_u32 *) qmalloc(100, sizeof(e_u32), __FILE__,__LINE__);
	reinit_ivec(p,p->iv[0],100,1);
	reinit_ivec(p,p->iv[1],100,1);
	
	// allocate 3 vectors in vector array "m2"
	int i = 0;
	for (i = 1; i < 4; i++) {
		p->m2[i] = (e_fp *) 
			qmalloc(64*64+4, sizeof(e_fp),  __FILE__,__LINE__ );	

		reinit_vec(p, p->m2[i], 64*64+4);
	}

	for (i = 2; i < 6; i++)
		p->v[i]=(e_fp *) qmalloc(p->Nact, sizeof(e_fp), __FILE__,__LINE__ );
	
	for (i = 0; i < p->Nact; i++) {
		p->v[2][i] = 0.0;
		p->v[3][i] = 0.5;
		p->v[4][i] = 0.0;
		p->v[5][i] = 0.5;
	}
	
	return;
}

e_fp pic_2d(test_params *p) {

	e_fp *y=p->v[0],  *z=p->v[1]; 
	e_fp *p1r=p->v[2],  *p1i=p->v[3]; 
	e_fp *p2r=p->v[4],  *p2i=p->v[5]; 
	e_u32 *e=p->iv[0],  *f=p->iv[1];
	e_fp *b=p->m2[1],  *c=p->m2[2],  *h=p->m2[3];

	int n=p->Nact, loop=p->Loop,l,ip;
	int i1=0,j1=0,i2=0,j2=0;
	
	// if (p->m2size < 64*64) {
		// p->err="pic_2d bad size for matrix";
		// return FPCONST(0.0);
	// }
	// if ((p->vsize < (64+32)) || (p->ivsize < (64+32))) {
		// p->err="pic_2d bad size for vectors";
		// return FPCONST(0.0);
	// }
	
   /*
     *******************************************************************
     *   Kernel 13 -- 2-D PIC (Particle In Cell)
     *******************************************************************
     *    DO  13     L= 1,Loop
     *    DO  13    ip= 1,n
     *              i1= P(1,ip)
     *              j1= P(2,ip)
     *              i1=        1 + MOD2N(i1,64)
     *              j1=        1 + MOD2N(j1,64)
     *         P(3,ip)= P(3,ip)  + B(i1,j1)
     *         P(4,ip)= P(4,ip)  + C(i1,j1)
     *         P(1,ip)= P(1,ip)  + P(3,ip)
     *         P(2,ip)= P(2,ip)  + P(4,ip)
     *              i2= P(1,ip)
     *              j2= P(2,ip)
     *              i2=            MOD2N(i2,64)
     *              j2=            MOD2N(j2,64)
     *         P(1,ip)= P(1,ip)  + Y(i2+32)
     *         P(2,ip)= P(2,ip)  + Z(j2+32)
     *              i2= i2       + E(i2+32)
     *              j2= j2       + F(j2+32)
     *        H(i2,j2)= H(i2,j2) + 1.0
     * 13 CONTINUE
     */
	 
	// reinit_vec_const(p,p1r,n,FPCONST(1.0));	// v[2]
	// reinit_vec_const(p,p1i,n,FPCONST(0.5));	// v[3]
	// reinit_vec_const(p,p2r,n,FPCONST(1.0));	// v[4]
	// reinit_vec_const(p,p2i,n,FPCONST(0.5));	// v[5]
	
	// reinit_vec(p,b,64*64+4);	// m2[0]
	// reinit_vec(p,c,64*64+4);	// m2[1]
	// reinit_vec(p,h,64*64+4);	// m2[3]
	
	// reinit_vec(p,y,100);		// v[0]
	// reinit_vec(p,z,100);		// v[1]
	// reinit_ivec(p,e,100,1);	// iv[0]
	// reinit_ivec(p,f,100,1);	// iv[1]
	
    for ( l=1 ; l<=loop ; l++ ) {
        for ( ip=0 ; ip<n ; ip++ ) {
            i1 = p1r[ip];
            j1 = p1i[ip];
            i1 &= 0x03f;
            j1 &= 0x03f;
            p2r[ip] += b[j1*64+i1];
            p2i[ip] += c[j1*64+i1];
            p1r[ip] += p2r[ip];
            p1i[ip] += p2i[ip];
            i2 = p1r[ip];
            j2 = p1i[ip];
            i2 &=0x03f;
            j2 &=0x03f;
            p1r[ip] += y[i2+32];
            p1i[ip] += z[j2+32];
            i2 += e[i2+32];
            j2 += f[j2+32];
            h[j2*64+i2] += 1.0;
        }
    }
	return h[0]+p1r[0]+p1i[0]+p2r[0]+p2i[0];
}

void pic_2d_fini(test_params *p) {

	int i = 0;
	for (i = 0; i < 6; i++)
		free(p->v[i]);

	for (i = 0; i < 2; i++)
		free(p->iv[i]);
		
	for (i = 1; i < 4; i++)
		free(p->m2[i]);
		// free working data

	return;
}

/******************************************************************************/
/*   Kernel 14 -- 1-D PIC (pticle In Cell)									  */
/******************************************************************************/

static e_u32 pow2(e_u32 x) {
    --x;
    x |= x >> 1;
    x |= x >> 2;
    x |= x >> 4;
    x |= x >> 8;
    x |= x >> 16;
	x++;
    return (x>>1);
}

void pic_1d_init(test_params *p) {

	p->Nact = pow2(p->N);
	//p->Nact = p->N;
	//xil_printf("%d\r\n", p->Nact);
	
	// allocate 2 vectors in vector array "v"
	int i = 0;
	for (i = 0; i < 10; i++) {
		p->v[i]=(e_fp *) qmalloc((p->Nact), sizeof(e_fp), __FILE__,__LINE__ );		
		zero_vec(p->v[i], (p->Nact));
	}	

	for (i = 0; i < 2; i++)
		p->iv[i] = (e_u32 *) 
			qmalloc(p->Nact, sizeof(e_u32), __FILE__,__LINE__ );
	
	reinit_vec(p, p->v[6], p->Nact);
	reinit_ivec_pos(p, p->iv[0], p->Nact, p->Nact - 1);
	return;
}

e_fp pic_1d(test_params *p) {

	e_fp *vx=p->v[0],  *xx=p->v[1]; 
	e_fp *grd=p->v[2],  *xi=p->v[3]; 
	e_fp *ex=p->v[4],  *ex1=p->v[5]; 
	e_fp *dex=p->v[6],  *dex1=p->v[7]; 
	e_fp *rx=p->v[8];
	e_fp *rh=p->v[9]; 
	e_u32 *ix=p->iv[0],  *ir=p->iv[1]; /* temporary outputs */
	e_fp dw,flx;
	int n=p->Nact, loop=p->Loop;
	int k,l;
	
	// for ( k=0 ; k<5 ; k++ ) {
		// printf("%d\n", ix[k]);
	// }
	
    /*
     *******************************************************************
     *   Kernel 14 -- 1-D PIC (pticle In Cell)
     *******************************************************************
     *    DO   14   L= 1,Loop
     *    DO   141  k= 1,n
     *          VX(k)= 0.0
     *          XX(k)= 0.0
     *          IX(k)= INT(  GRD(k))
     *          XI(k)= e_fp( IX(k))
     *         EX1(k)= EX   ( IX(k))
     *        DEX1(k)= DEX  ( IX(k))
     *41  CONTINUE
     *    DO   142  k= 1,n
     *          VX(k)= VX(k) + EX1(k) + (XX(k) - XI(k))*DEX1(k)
     *          XX(k)= XX(k) + VX(k)  + FLX
     *          IR(k)= XX(k)
     *          RX(k)= XX(k) - IR(k)
     *          IR(k)= MOD2N(  IR(k),2048) + 1
     *          XX(k)= RX(k) + IR(k)
     *42  CONTINUE
     *    DO  14    k= 1,n
     *    RH(IR(k)  )= RH(IR(k)  ) + 1.0 - RX(k)
     *    RH(IR(k)+1)= RH(IR(k)+1) + RX(k)
     *14  CONTINUE
     */
	 
	//if (!is_2px(n))
		//mexit("Input for pic_1d in loops must be power of 2", __FILE__,__LINE__ );
	
	//reinit_ivec_pos(p, ix, n, n-1); /* n 1..n numbers */
	//reinit_vec(p, dex, n); /* n 1..n numbers */
	
	dw= -100.000e0;
    for ( k=0 ; k<n ; k++ ) {
		dex[k] =  dw*dex[k];
		grd[k] = ix[k];
	}
	flx= 0.00100e0;

    for ( l=1 ; l<=loop ; l++ ) {
	
        for ( k=0 ; k<n ; k++ ) {
            vx[k] = 0.0;
            xx[k] = 0.0;
            ix[k] = (long) grd[k];
            xi[k] = (e_fp) ix[k];
            ex1[k] = ex[ ix[k] - 1 ];
            dex1[k] = dex[ ix[k] - 1 ];
        }
		
        for ( k=0 ; k<n ; k++ ) {
            vx[k] += ex1[k] + ( xx[k] - xi[k] )*dex1[k];
            xx[k] += vx[k]  + flx;
            ir[k] = (e_u32)xx[k];
            rx[k] = xx[k] - ir[k];
            ir[k] = ( ir[k] & (n-1) ) + 1;
            xx[k] = rx[k] + ir[k];
        }
		
        for ( k=0 ; k<n ; k++ ) {
            rh[ ir[k]-1 ] += 1.0 - rx[k];
            rh[ ir[k]   ] += rx[k];
        }
    }
	return rh[0];
}

void pic_1d_fini(test_params *p) {

	int i = 0;
	for (i = 0; i < 10; i++)
		free(p->v[i]);	

	for (i = 0; i < 2; i++)
		free(p->iv[i]);
		// free working data

	return;
}

/******************************************************************************/
/*   Kernel 18 - 2-D explicit hydrodynamics fragment						  */
/******************************************************************************/

void hydro_2d_init(test_params *p) {

	// params->nsize[HYDRO_2D_IDX]=params->N/7;
	p->Nact = p->N/7;
	
	// allocate 9 vectors in vector array "m2"
	int i = 0;
	for (i = 0; i < 10; i++)
		p->m2[i] = (e_fp *) 
			qmalloc(((p->Nact)*7), sizeof(e_fp),  __FILE__,__LINE__ );
			// IMPORTANT: correct sizes??
	
	reinit_vec(p, p->m2[2], (p->Nact)*7);
	reinit_vec(p, p->m2[3], (p->Nact)*7);
	reinit_vec(p, p->m2[4], (p->Nact)*7);
	reinit_vec(p, p->m2[5], (p->Nact)*7);
	reinit_vec(p, p->m2[8], (p->Nact)*7);

	for (i = 0; i < (p->Nact)*7; i++) {
		p->m2[6][i] = 0.0;
		p->m2[7][i] = 0.0;
	}

	return;
}

e_fp hydro_2d(test_params *p) {

	e_fp *za=p->m2[0];
	e_fp *zb=p->m2[1];
	e_fp *zp=p->m2[2];
	e_fp *zq=p->m2[3];
	e_fp *zm=p->m2[4];
	e_fp *zr=p->m2[5];
	e_fp *zv=p->m2[6];
	e_fp *zu=p->m2[7];
	e_fp *zz=p->m2[8];
	e_fp t=0.0,s=0.0, ret=0.0;
	int n=p->Nact, loop=p->Loop;
	int j,k,l;
    
    /*
     *******************************************************************
     *   Kernel 18 - 2-D explicit hydrodynamics fragment
     *******************************************************************
     *       DO 75  L= 1,Loop
     *              T= 0.0037
     *              S= 0.0041
     *             KN= 6
     *             JN= n
     *       DO 70  k= 2,KN
     *       DO 70  j= 2,JN
     *        ZA(j,k)= (ZP(j-1,k+1)+ZQ(j-1,k+1)-ZP(j-1,k)-ZQ(j-1,k))
     *   .            *(ZR(j,k)+ZR(j-1,k))/(ZM(j-1,k)+ZM(j-1,k+1))
     *        ZB(j,k)= (ZP(j-1,k)+ZQ(j-1,k)-ZP(j,k)-ZQ(j,k))
     *   .            *(ZR(j,k)+ZR(j,k-1))/(ZM(j,k)+ZM(j-1,k))
     * 70    CONTINUE
     *       DO 72  k= 2,KN
     *       DO 72  j= 2,JN
     *        ZU(j,k)= ZU(j,k)+S*(ZA(j,k)*(ZZ(j,k)-ZZ(j+1,k))
     *   .                    -ZA(j-1,k) *(ZZ(j,k)-ZZ(j-1,k))
     *   .                    -ZB(j,k)   *(ZZ(j,k)-ZZ(j,k-1))
     *   .                    +ZB(j,k+1) *(ZZ(j,k)-ZZ(j,k+1)))
     *        ZV(j,k)= ZV(j,k)+S*(ZA(j,k)*(ZR(j,k)-ZR(j+1,k))
     *   .                    -ZA(j-1,k) *(ZR(j,k)-ZR(j-1,k))
     *   .                    -ZB(j,k)   *(ZR(j,k)-ZR(j,k-1))
     *   .                    +ZB(j,k+1) *(ZR(j,k)-ZR(j,k+1)))
     * 72    CONTINUE
     *       DO 75  k= 2,KN
     *       DO 75  j= 2,JN
     *        ZR(j,k)= ZR(j,k)+T*ZU(j,k)
     *        ZZ(j,k)= ZZ(j,k)+T*ZV(j,k)
     * 75    CONTINUE
     */

	int kn = 6;
	int jn = n;

    for ( l=1 ; l<=loop ; l++ ) {
        t = 0.0037;
        s = 0.0041;
		for ( k=1 ; k<kn ; k++ ) {
			for ( j=1 ; j<jn ; j++ ) {
				int up=(k-1)*jn+j;
				int down=(k+1)*jn+j;
				int cur=k*jn+j;
				za[cur] = ( zp[down-1] +zq[down-1] -zp[cur-1] -zq[cur-1] )*
					( zr[cur] +zr[cur-1] ) / ( zm[cur-1] +zm[down-1]);
				zb[cur] = ( zp[cur-1] +zq[cur-1] -zp[cur] -zq[cur] ) *
					( zr[cur] +zr[up] ) / ( zm[cur] +zm[cur-1]);
			}
		}
		
        for ( k=1 ; k<kn ; k++ ) {
            for ( j=1 ; j<jn ; j++ ) {
				int up=(k-1)*jn+j;
				int down=(k+1)*jn+j;
				int cur=k*jn+j;
                zu[cur] += s*( za[cur]   *( zz[cur] - zz[cur+1] ) -
                                za[cur-1] *( zz[cur] - zz[cur-1] ) -
                                zb[cur]   *( zz[cur] - zz[up] ) +
                                zb[down] *( zz[cur] - zz[down] ) );
                zv[cur] += s*( za[cur]   *( zr[cur] - zr[cur+1] ) -
                                za[cur-1] *( zr[cur] - zr[cur-1] ) -
                                zb[cur]   *( zr[cur] - zr[up] ) +
                                zb[down] *( zr[cur] - zr[down] ) );
            }
        }
		
        for ( k=1 ; k<kn ; k++ ) {
            for ( j=1 ; j<jn ; j++ ) {
				int cur=k*jn+j;
                zr[cur] = zr[cur] + t*zu[cur];
                zz[cur] = zz[cur] + t*zv[cur];
            }
        }
    }
	
	ret += get_array_feedback(zr,kn*jn);
	ret += get_array_feedback(zz,kn*jn);
	
	return ret;
}

void hydro_2d_fini(test_params *p) {

	int i = 0;
	for (i = 0; i < 10; i++)
		free(p->m2[i]);	
		// free working data

	return;
}

/******************************************************************************/
/*   Kernel 19 -- general linear recurrence equations						  */
/******************************************************************************/
void lin_recurrence_init(test_params *p) {

	p->Nact = p->N;
	
	// allocate 3 vectors in vector array "v"
	int i = 0;
	for (i = 0; i < 3; i++)
		p->v[i]=(e_fp *) qmalloc((p->Nact), sizeof(e_fp), __FILE__,__LINE__ );
	
	reinit_vec(p, p->v[0], (p->Nact));
	reinit_vec(p, p->v[1], (p->Nact));
	return;
}

e_fp lin_recurrence(test_params *p) {

	e_fp *sa=p->v[0],  *sb=p->v[1]; 
	e_fp *b5=p->v[2]; 
	e_fp stb5;
	int n=p->Nact, loop=p->Loop;
	
       /*
     *******************************************************************
     *   Kernel 19 -- general linear recurrence equations
     *******************************************************************
     *               KB5I= 0
     *           DO 194 L= 1,Loop
     *           DO 191 k= 1,n
     *         B5(k+KB5I)= SA(k) +STB5*SB(k)
     *               STB5= B5(k+KB5I) -STB5
     *191        CONTINUE
     *192        DO 193 i= 1,n
     *                  k= n-i+1
     *         B5(k+KB5I)= SA(k) +STB5*SB(k)
     *               STB5= B5(k+KB5I) -STB5
     *193        CONTINUE
     *194 CONTINUE
     */
	 
	int kb5i = 0,l,i,k;
	
	// reinit_vec(p,sa,n);
	// reinit_vec(p,sb,n);
	
	stb5=sa[0]/sa[1];/* small random number */
    for ( l=1 ; l<=loop ; l++ ) {

        for ( k=0 ; k<n ; k++ ) {
            b5[k+kb5i] = sa[k] + stb5*sb[k];
            stb5 = b5[k+kb5i] - stb5;
        }
		
        for ( i=1 ; i<=n ; i++ ) {
            k = n - i ;
            b5[k+kb5i] = sa[k] + stb5*sb[k];
            stb5 = b5[k+kb5i] - stb5;
        }
   }
	return stb5;
}

void lin_recurrence_fini(test_params *p) {

	int i = 0;
	for (i = 0; i < 3; i++)
		free(p->v[i]);	
		// free working data

	return;
}

/******************************************************************************/
/*   Kernel 21 -- matrix*matrix product										  */
/******************************************************************************/
void matmul_init(test_params *p) {

	// params->nsize[MATMUL_IDX]=params->N/25;
	// params->m2size=params->N*4+4;
	// params->m2size+=params->Loop;
	
	//p->Nact = (p->N*4 + p->Loop);

	p->Nact = p->N/25;
	
	// allocate 3 vectors in vector array "m2"
	p->m2[0]=(e_fp *) qmalloc((p->N), sizeof(e_fp), __FILE__,__LINE__ );
	p->m2[1]=(e_fp *) qmalloc((p->N), sizeof(e_fp), __FILE__,__LINE__ );
	p->m2[2]=(e_fp *) qmalloc((p->Loop + p->N), sizeof(e_fp), __FILE__,__LINE__ );

	zero_vec(p->m2[0], (p->N));
	reinit_vec(p, p->m2[1], (p->N));
	reinit_vec(p, p->m2[2], (p->Loop + p->N));
	
	return;
}

e_fp matmul(test_params *p) {
	
	e_fp *px=p->m2[0];
	e_fp *vy=p->m2[1];
	e_fp *cx=p->m2[2];
	int n=p->Nact, loop=p->Loop;
	int i,j,k,l;
	e_fp ret;
	
	// zero_vec(px,25*n);	
	//reinit_vec_limited(p,vy,25*n);
	//reinit_vec_limited(p,cx,25*n+loop);
	
   /*
     *******************************************************************
     *   Kernel 21 -- matrix*matrix product
     *******************************************************************
     *    DO 21 L= 1,Loop
     *    DO 21 k= 1,25
     *    DO 21 i= 1,25
     *    DO 21 j= 1,n
     *    PX(i,j)= PX(i,j) +VY(i,k) * CX(k,j)
     * 21 CONTINUE
     */
	/*SG: Add dependency on outer loop to CX so compiler cannot optimize it away to a multiply on the final result. */
	
    for ( l=1 ; l<=loop ; l++ ) {
        for ( k=0 ; k<25 ; k++ ) {
            for ( i=0 ; i<25 ; i++ ) {
                for ( j=0 ; j<n ; j++ ) {
                    px[j*25+i] += vy[k*n+i] * cx[j*25+k+l];
                }
            }
        }
    }
	ret=get_array_feedback(px,n*25);
	return ret;
}

void matmul_fini(test_params *p) {

	int i = 0;
	for (i = 0; i < 3; i++)
		free(p->m2[i]);	
		// free working data

	return;
}

/******************************************************************************/
/*   Kernel 23 -- 2-D implicit hydrodynamics fragment						  */
/******************************************************************************/
void hydro_2d_implicit_init(test_params *p) {

	// params->m2size=params->N*4+4;
	// params->m2size+=params->Loop;
	// params->nsize[HYDRO_2D_IMPLICIT_IDX]=params->N/6;

	p->Nact = p->N/6;
	
	p->m2[0]=(e_fp *) qmalloc((p->N + p->Nact), sizeof(e_fp), __FILE__,__LINE__ );
	zero_vec(p->m2[0], (p->N));
	
	// allocate 6 vectors in vector array "m2"
	int i = 0;
	for (i = 1; i < 6; i++) {
		p->m2[i]=(e_fp *) qmalloc((p->N), sizeof(e_fp), __FILE__,__LINE__ );
		reinit_vec(p, p->m2[i], (p->N));
	}	
	
	return;
}

e_fp hydro_2d_implicit(test_params *p) {
	
	e_fp *za=p->m2[0];
	e_fp *zr=p->m2[1];
	e_fp *zu=p->m2[2];
	e_fp *zb=p->m2[3];
	e_fp *zv=p->m2[4];
	e_fp *zz=p->m2[5];
	int n=p->Nact, loop=p->Loop;
	int j,k,l;
	e_fp ret;
	
	// zero_vec(za,6*n);
	// reinit_vec(p,zr,6*n);
	// reinit_vec(p,zb,6*n);
	// reinit_vec(p,zz,6*n);
	// reinit_vec(p,zv,6*n);
	// reinit_vec(p,zu,6*n);
	
    /*
     *******************************************************************
     *   Kernel 23 -- 2-D implicit hydrodynamics fragment
     *******************************************************************
     *    DO 23  L= 1,Loop
     *    DO 23  j= 2,6
     *    DO 23  k= 2,n
     *          QA= ZA(k,j+1)*ZR(k,j) +ZA(k,j-1)*ZB(k,j) +
     *   .          ZA(k+1,j)*ZU(k,j) +ZA(k-1,j)*ZV(k,j) +ZZ(k,j)
     * 23  ZA(k,j)= ZA(k,j) +.175*(QA -ZA(k,j))
     */

    for ( l=1 ; l<=loop ; l++ ) {
        for ( j=1 ; j<6 ; j++ ) {
            for ( k=1 ; k<n ; k++ ) {
				int cur=j*n+k;
				int up=(j-1)*n+k;
				int down=(j+1)*n+k;
                e_fp qa = za[down]*zr[cur] + za[up]*zb[cur] +
                     za[cur+1]*zu[cur] + za[cur-1]*zv[cur] + zz[cur];
                za[cur] += 0.175*( qa - za[cur] );
            }
        }
    }
	ret=get_array_feedback(za,n); /* return a value that depends on each compute in the loop */
	return ret;
}	

void hydro_2d_implicit_fini(test_params *p) {

	int i = 0;
	for (i = 0; i < 6; i++)
		free(p->m2[i]);	
		// free working data

	return;
}

/******************************************************************************/
/*   Kernel 5 -- tri-diagonal elimination, below diagonal					  */
/******************************************************************************/
void tri_diagonal_init(test_params *p) {

	p->Nact = p->N;
	
	// allocate 3 vectors in vector array "v"
	int i = 0;
	for (i = 0; i < 3; i++) {
		p->v[i]=(e_fp *) qmalloc((p->Nact), sizeof(e_fp), __FILE__,__LINE__ );
		reinit_vec(p,p->v[i],p->Nact);
	}	
	
	return;
}

e_fp tri_diagonal(test_params *p) {

	e_fp *x=p->v[0],  *y=p->v[1],  *z=p->v[2];
	int n=p->Nact, loop=p->Loop;
	int i,l;
	e_fp ret;
	
	// reinit_vec(p,x,n);
	// reinit_vec(p,y,n);
	// reinit_vec(p,z,n);
	
    /*
     *******************************************************************
     *   Kernel 5 -- tri-diagonal elimination, below diagonal
     *******************************************************************
     *    DO 5 L = 1,Loop
     *    DO 5 i = 2,n
     *  5    X(i)= Z(i)*(Y(i) - X(i-1))
     */

    for ( l=1 ; l<=loop ; l++ ) {
        for ( i=1 ; i<n ; i++ ) {
            x[i] = z[i]*( y[i] - x[i-1] ); 
        }
    }
	
	ret=get_array_feedback(x,n);
	return ret;
}

void tri_diagonal_fini(test_params *p) {

	int i = 0;
	for (i = 0; i < 3; i++)
		free(p->v[i]);	
		// free working data

	return;
}

/******************************************************************************/
/******************************************************************************/

// init any one of the data arrays available in the harness (harness.h)
benchfunc_init kernelInit[] = {
	hydro_fragment_init,
	cholesky_init,
	inner_product_init,
	linear_recurrence_init,
	state_fragment_init,
	integrate_predictors_init,
	difference_predictors_init,
	pic_2d_init,
	pic_1d_init,
	hydro_2d_init,
	lin_recurrence_init,
	matmul_init,
	hydro_2d_implicit_init,
	tri_diagonal_init,
	NULL
};

// calls to kernels
benchfunc kernels[] = {
	hydro_fragment,
	cholesky,
	inner_product,
	linear_recurrence,
	state_fragment,
	integrate_predictors,
	difference_predictors,
	pic_2d,
	pic_1d,
	hydro_2d,
	lin_recurrence,
	matmul,
	hydro_2d_implicit,
	tri_diagonal,
	NULL
};

// free previously init'ed arrays
benchfunc_fini kernelFini[] = {
	hydro_fragment_fini,
	cholesky_fini,
	inner_product_fini,
	linear_recurrence_fini,
	state_fragment_fini,
	integrate_predictors_fini,
	difference_predictors_fini,
	pic_2d_fini,
	pic_1d_fini,
	hydro_2d_fini,
	lin_recurrence_fini,
	matmul_fini,
	hydro_2d_implicit_fini,
	tri_diagonal_fini,
	NULL
};

char *kernelnames[] = {
	"HYDRO",
	"CHOLESKY",
	"INNER_PROD",
	"G_LINEAR_RECURRENCE",
	"STATE_FRAG",
	"INTEGRATE_PREDICT",
	"DIFFERENCE_PREDICT",
	"PIC_2D",
	"PIC_1D",
	"HYDRO_2D",
	"LIN_REC",
	"MATMUL",
	"HYDRO_2D_IMPLICIT",
	"TRI_DIAG"
}; 
