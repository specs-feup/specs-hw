#include "types.h"
#include "harness.h"
#include "kernels.h"

#ifndef DESKTOP
#include "reference.h"
#endif

#ifdef DESKTOP

int main(int argc, char *argv[])
{
	e_u32 	seed = 0x4645ca3, N = 0x1000, 
			Loop = 0x1, tests = HYDRO, gen_ref = 0, verify = 0;

	test_params sparams;
	test_params *params;	
	
//	mmemset((void*)(&sparams), 0, sizeof(test_params));
	params = &sparams;
			
	//parse switches
	int c = 0;	
	while ((c = getopt(argc, argv, "s:n:x:l:gv")) != -1) {
		switch(c) {
		case 's':
			sscanf(optarg, "%d", &seed);
			break;
		case 'n':
			sscanf(optarg, "%d", &N);
			break;
		case 'x':
			sscanf(optarg, "%x", &tests);
			break;
		case 'l':
			sscanf(optarg, "%d", &Loop);
			break;
		case 'g':
			gen_ref = 1;
			break;			
		case 'v':
			verify = 1;
			break;		
		//case '?':
		default:
			break;
		}
	}

	// define parameters of tests
	sparams = initParameters_fromWorkLoad(
		LAST_TEST_IDX, kernelnames, kernels, kernelInit, kernelFini,
		
		// arguments
		tests, N, Loop, seed, gen_ref, verify, 0
	);	

#else

int main()
{
	test_params sparams;
//	mmemset((void*)(&sparams), 0, sizeof(test_params));
	
	// init tests from preset in memory (includes reference data)
	test_params *params = &sparams;	
	init_preset_0(params);

	initParameters_fromPreset(LAST_TEST_IDX,
			kernelnames, kernels, kernelInit, kernelFini, params);

#endif

#if SIM==1

	// do test
	params->Nact = params->N;
	kernelInit[WHICHTEST](params);
	kernels[WHICHTEST](params);

#else
	
	printFunc("\r\nCalling tests...\r\n");

	// do tests
	runAll((void *)(params));
	
	// print results
	printResults(params);
	
	// end
	printFunc("\r\n");

#endif
	return 0;
}
