package org.specs.MicroBlaze.provider;

public enum MicroBlazePolyBenchSmallInt implements MicroBlazeELFProvider {

    /*
     * output of mb-objdump -t:
     * 
    00000710 g     F .text  00000130 kernel_lu
    00000fa4 g     F .text  00000184 kernel_jacobi_1d
    0000053c g     F .text  00000160 kernel_nussinov
    00001768 g     F .text  000001fc kernel_ludcmp
    00000650 g     F .text  000000d4 kernel_mvt
    00000614 g     F .text  00000110 kernel_trmm
    000013f4 g     F .text  000001c0 kernel_gemver
    00000624 g     F .text  000000a4 kernel_floyd_warshall
    00000678 g     F .text  0000015c kernel_symm
    0000053c g     F .text  000000d8 kernel_trisolv
    00000634 g     F .text  0000011c kernel_syr2k
    000005ec g     F .text  00000100 kernel_syrk
    00000668 g     F .text  00000114 kernel_gemm
     */
    floydwarshall(0x0624L, 0x6C4L),
    gemm(0x0668L, 0x778L),
    gemver(0x13f4L, 0x15B0L),
    jacobi1d(0x0fa4L, 0x1124L),
    lu(0x0710L, 0x83CL),
    ludcmp(0x1768L, 0x1960L),
    mvt(0x0650L, 0x720L),
    nussinov(0x053cL, 0x698L),
    symm(0x0678L, 0x7D0L),
    syr2k(0x0634L, 0x74CL),
    syrk(0x05ecL, 0x6E8L),
    trisolv(0x053cL, 0x610L),
    trmm(0x0614L, 0x720L);

    private String elfName;
    private Number kernelStart;
    private Number kernelStop;

    private MicroBlazePolyBenchSmallInt(Number kernelStart, Number kernelStop) {
        this.elfName = name() + ".elf";
        this.kernelStart = kernelStart;
        this.kernelStop = kernelStop;
    }

    @Override
    public Number getKernelStart() {
        return kernelStart;
    }

    @Override
    public Number getKernelStop() {
        return kernelStop;
    }

    @Override
    public String getELFName() {
        return this.elfName;
    }
}
