#include <stdlib.h>
#include <iostream>
#include <verilated.h>
#include <verilated_vcd_c.h>

#include "V<TESTBENCHNAME>.h"

#define VALIDATION_SAMPLES <NUMBEROFSAMPLES>
#define VERIFICATION_FAIL 0
#define VERIFICATION_OK 1

int main(int argc, char **argv)
{
  V<TESTBENCHNAME> *tb = new V<TESTBENCHNAME>;

  for(int i = VALIDATION_SAMPLES; i > 0; i--){

    tb->verify = 1;

    for(int w = 0; w < 100; w++)
      tb->eval();

    tb->verify = 0;
    tb->eval();

    if(tb->verifyResults == VERIFICATION_FAIL) {
      delete tb;
      std::cout << "FAILED\n";
      exit(EXIT_SUCCESS);
    }

  }

  delete tb;
  std::cout << "PASSED\n";

  exit(EXIT_SUCCESS);

}