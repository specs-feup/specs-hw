static int a[100];
static int b[100];

int main() {
	volatile int result = 0;
	for(int i = 0; i < 100; i++)
		result += a[i] * b[i];

	return result;
}
