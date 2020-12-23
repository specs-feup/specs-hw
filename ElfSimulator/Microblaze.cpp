#include Microblaze.h

int pc = -1;
int r2 = 0;
int r3 = 0;
int r4 = 0;
int r0 = 0;
int r1 = 0;



int imm(IMM){
	RD = RA;

}

int brai(IMM){
	pc = sext(IMM);

}

int bri(IMM){
	pc = pc+sext(IMM);

}


 /*MICROBLAZE*/