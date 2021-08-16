/**
 * This version is stamped on May 10, 2016
 *
 * Contact:
 *   Louis-Noel Pouchet <pouchet.ohio-state.edu>
 *   Tomofumi Yuki <tomofumi.yuki.fr>
 *
 * Web address: http://polybench.sourceforge.net
 */
/* durbin.c: this file is part of PolyBench/C */

//#include <stdio.h>
//#include <unistd.h>
//#include <string.h>
//#include <math.h>

/* Include polybench common header. */
#include <polybench.h>

/* Include benchmark-specific header. */
#include "durbin.h"

/* Include polybench custom header. */
#include "polybenchBaremetal.h"

/* Array initialization. */
//static
void init_array (int n,
		 DATA_TYPE POLYBENCH_1D(r,N,n))
{
  int i, j;

  for (i = 0; i < n; i++)
    {
      r[i] = (n+1-i);
    }
}

/* Main computational kernel. The whole function will be timed,
   including the call and return. */
//static
void kernel_durbin(int n,
		   DATA_TYPE POLYBENCH_1D(r,N,n),
		   DATA_TYPE POLYBENCH_1D(y,N,n))
{
 DATA_TYPE z[N];
 DATA_TYPE alpha;
 DATA_TYPE beta;
 DATA_TYPE sum;

 int i,k;

#pragma scop
 y[0] = -r[0];
 beta = SCALAR_VAL(1.0);
 alpha = -r[0];

 for (k = 1; k < _PB_N; k++) {
   beta = (1-alpha*alpha)*beta;
   sum = SCALAR_VAL(0.0);
   for (i=0; i<k; i++) {
      sum += r[k-i-1]*y[i];
   }
   alpha = - (r[k] + sum)/beta;

   for (i=0; i<k; i++) {
      z[i] = y[i] + alpha*y[k-i-1];
   }
   for (i=0; i<k; i++) {
     y[i] = z[i];
   }
   y[k] = alpha;
 }
#pragma endscop

}


int main(int argc, char** argv)
{
  /* Retrieve problem size. */
  int n = N;

  /* Variable declaration/allocation. */
  POLYBENCH_1D_ARRAY_DECL(r, DATA_TYPE, N, n);
  POLYBENCH_1D_ARRAY_DECL(y, DATA_TYPE, N, n);


  /* Initialize array(s). */
  init_array (n, POLYBENCH_ARRAY(r));

  /* Start timer. */
  polybench_start_instruments;

  /* Run kernel. */
  kernel_durbin (n,
		 POLYBENCH_ARRAY(r),
		 POLYBENCH_ARRAY(y));

  /* Stop and print timer. */
  polybench_stop_instruments;
  polybench_print_instruments;

  /* Prevent dead-code elimination. All live-out data must be printed
     by the function call in argument. */
  polybench_prevent_dce(hashArray1D(n, POLYBENCH_ARRAY(y)));

  /* Be clean. */
  POLYBENCH_FREE_ARRAY(r);
  POLYBENCH_FREE_ARRAY(y);

  return 0;
}
