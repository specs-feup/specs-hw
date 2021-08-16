#include <stdio.h>
#include <math.h>

#define N 100

void init_array (int n,
		 float *float_n,
		 float a[N]) {
  int i;
  *float_n = (float)n;
  for (i = 0; i < N; i++)
    a[i] = ((float) i) / N;
}

float hashArray1D(int n, float a[N]) {

	int i;
	float ret=0.0;
	if (n==0) return 0.0;
	for (i=0; i<n; i++) {
		if (i&1) ret+=a[i];
		else ret-=a[i];
	}

	return ret/(float)n;
}

int main() {
	int n = N;
	float float_n;
	float data[N];
	init_array (n, &float_n, data);
	printf("%f\n", hashArray1D(n, data));
	return 0;
}

