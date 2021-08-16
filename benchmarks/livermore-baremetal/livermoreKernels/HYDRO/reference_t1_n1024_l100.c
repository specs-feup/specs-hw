/**** START DATASET ****/
#include "types.h"
#include "harness.h"
static intparts ref_data_index[]={
	{1,28,0x00000000,0x00eea6b6}/*-5.004878720000000000e+08*/,
	{0,0,0x00000000,0x00000000}/*0.000000000000000000e+00*/,
	{0,0,0x00000000,0x00000000}/*0.000000000000000000e+00*/,
	{0,0,0x00000000,0x00000000}/*0.000000000000000000e+00*/,
	{0,0,0x00000000,0x00000000}/*0.000000000000000000e+00*/,
	{0,0,0x00000000,0x00000000}/*0.000000000000000000e+00*/,
	{0,0,0x00000000,0x00000000}/*0.000000000000000000e+00*/,
	{0,0,0x00000000,0x00000000}/*0.000000000000000000e+00*/,
	{0,0,0x00000000,0x00000000}/*0.000000000000000000e+00*/,
	{0,0,0x00000000,0x00000000}/*0.000000000000000000e+00*/,
	{0,0,0x00000000,0x00000000}/*0.000000000000000000e+00*/,
	{0,0,0x00000000,0x00000000}/*0.000000000000000000e+00*/,
	{0,0,0x00000000,0x00000000}/*0.000000000000000000e+00*/,
	{0,0,0x00000000,0x00000000}/*0.000000000000000000e+00*/,
	{0,0,0x00000000,0x00000000}/*0.000000000000000000e+00*/
}; /* ref_data */

void init_preset_0(test_params *params) {
	params->maxnumkernels = 14;
	params->seed=0x4645ca3;
	params->tests=0x00000001;
	params->N=0x100;
	params->Loop=0x01;
	mmemcpy(&(params->ref_data), &(ref_data_index), sizeof(intparts) * 32);
}
/**** END DATASET ****/