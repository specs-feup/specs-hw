//#include <stdio.h>

volatile unsigned int * const UART0DR = (unsigned int *) 0x10000000;
 
void print_uart0(const char *s) {
    while(*s != '\0') { 		// Loop until end of string 
         *UART0DR = (unsigned int)(*s); // Transmit char 
          s++;			        // Next char 
    }
}
 
int main() {
	int i = 0, k = 0, j = 0;

	for(i = 0; i < 100; i++)
	     k += k + i * k;

	for(i = 0; i < 100; i++)
	     j += k/j + i;

	for(i = 0; i < 10; i++)
	     print_uart0("Hello world!\n");
	     
	return 0;
}

