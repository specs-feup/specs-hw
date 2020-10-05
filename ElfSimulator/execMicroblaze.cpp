#include Microblaze.h 
main(){

ADR0:
	imm(0);
		 printf("imm	 0x0");
ADR8:
	brai(128);
		 printf("brai	 0x80");
			 if(pc != -1){ goto [[TODO: Compute Lable from ParseTree]];}

ADR128:
	bri(0);
		 printf("bri	 0x0");
			 if(pc != -1){ goto [[TODO: Compute Lable from ParseTree]];}

}

 /* execMICROBLAZE*/