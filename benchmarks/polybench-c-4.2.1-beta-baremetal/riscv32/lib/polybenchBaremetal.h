#ifndef POLYBENCH_BAREMETAL_H
#define POLYBENCH_BAREMETAL_H

/* For some kernels, redefine some macro names */
#if defined _GEMM_H
#define M NI
#define N NJ
#endif

/* For some kernels, redefine some macro names */
#if defined _2MM_H || defined _3MM_H
#define M NI
#define N NL
#endif

/* For some kernels, redefine some macro names */
#ifdef _DOITGEN_H
#define M NR
#define N NQ
#define L NP
#endif

/* For some kernels, redefine some macro names */
#ifdef _DERICHE_H
#define M W
#define N H
#endif

/* For some kernels, redefine some macro names */
#ifdef _FDTD_2D_H
#define M NX
#define N NY
#endif

#ifdef N
/*
 * Use across all benchmarks for DCE prevention
 */  
static DATA_TYPE hashArray1D(int n, 
		DATA_TYPE POLYBENCH_1D(a, N, n)) {
	int i;
	DATA_TYPE ret=0.0;
	if (n==0) return 0.0;
	for (i=0; i<n; i++) {
		if (i&1) ret+=a[i];
		else ret-=a[i];
	}

	return ret/(DATA_TYPE)n;
}

/*
 * Use across all benchmarks for DCE prevention
 */  
static DATA_TYPE hashArray2D_NxN(int n, 
		DATA_TYPE POLYBENCH_2D(a,N,N,n,n)) {
	int i, j;
	DATA_TYPE ret=0.0;
	if (n==0) return 0.0;
	for (i = 0; i < n; i++)
	  for (j = 0; j < n; j++) {
	  	if (i&1) ret+=a[i][j];
		else ret-=a[i][j];
	  }

	return ret/(DATA_TYPE)n;
}

static DATA_TYPE hashArray3D_NxNxN(int n,
		 DATA_TYPE POLYBENCH_3D(a,N,N,N,n,n,n)) {
	int i, j, k;
	DATA_TYPE ret=0.0;
	if (n==0) return 0.0;
	for (i = 0; i < n; i++)
	  for (j = 0; j < n; j++)
	    for (k = 0; k < n; k++) {
		if (i&1) ret+=a[i][j][k];
		else ret-=a[i][j][k];
	  }

	return ret/(DATA_TYPE)n;
}
#endif

#ifdef M
/*
 * Use across all benchmarks for DCE prevention
 */  
static DATA_TYPE hashArray2D_MxM(int m, 
		DATA_TYPE POLYBENCH_2D(a,M,M,m,m)) {
	int i, j;
	DATA_TYPE ret=0.0;
	if (m==0) return 0.0;
	for (i = 0; i < m; i++)
	  for (j = 0; j < m; j++) {
	  	if (i&1) ret+=a[i][j];
		else ret-=a[i][j];
	  }

	return ret/(DATA_TYPE)m;
}
#endif

#if defined N && defined M

/*
 * Use across all benchmarks for DCE prevention
 */  
static DATA_TYPE hashArray2D_MxN(int m, int n, 
		DATA_TYPE POLYBENCH_2D(a,M,N,m,n)) {
	int i, j;
	DATA_TYPE ret=0.0;
	if (m==0) return 0.0;
	for (i = 0; i < m; i++)
	  for (j = 0; j < n; j++) {
	  	if (i&1) ret+=a[i][j];
		else ret-=a[i][j];
	  }

	return ret/(DATA_TYPE)m;
}
#endif

#if defined N && defined M && defined L

static DATA_TYPE hashArray3D_MxNxL(int m, int n, int l,
		 DATA_TYPE POLYBENCH_3D(a,M,N,L,m,n,l)) {
	int i, j, k;
	DATA_TYPE ret=0.0;
	if (m==0) return 0.0;
	for (i = 0; i < m; i++)
	  for (j = 0; j < n; j++)
	    for (k = 0; k < l; k++) {
		if (i&1) ret+=a[i][j][k];
		else ret-=a[i][j][k];
	  }

	return ret/(DATA_TYPE)m;
}
#endif

// (un-)define some polybench stuff
#ifdef POLYBENCH_DUMP_START
#undef POLYBENCH_DUMP_START
#define POLYBENCH_DUMP_START
#endif 

#ifdef POLYBENCH_DUMP_FINISH
#undef POLYBENCH_DUMP_FINISH
#define POLYBENCH_DUMP_FINISH
#endif 

#ifdef POLYBENCH_DUMP_BEGIN
#undef POLYBENCH_DUMP_BEGIN
#define POLYBENCH_DUMP_BEGIN(s)
#endif 

#ifdef POLYBENCH_DUMP_END
#undef POLYBENCH_DUMP_END
#define POLYBENCH_DUMP_END(s)
#endif 

#ifdef POLYBENCH_DCE_ONLY_CODE
#undef POLYBENCH_DCE_ONLY_CODE
#define POLYBENCH_DCE_ONLY_CODE
#endif 

#define polybench_start_instruments
#define polybench_stop_instruments
#define polybench_print_instruments

#endif //POLYBENCH_BAREMETAL_H

