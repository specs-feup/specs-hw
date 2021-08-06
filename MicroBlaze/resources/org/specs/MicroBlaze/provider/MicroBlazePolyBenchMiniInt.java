package org.specs.MicroBlaze.provider;

public enum MicroBlazePolyBenchMiniInt implements MicroBlazeELFProvider {

    /*
     * output of mb-objdump -t:
     * 
    000008dc g     F .text  00000130 kernel_lu
    00000ee4 g     F .text  00000184 kernel_jacobi_1d
    0000067c g     F .text  00000160 kernel_nussinov
    000017c8 g     F .text  000001fc kernel_ludcmp
    000005d8 g     F .text  000000d4 kernel_mvt
    0000059c g     F .text  00000110 kernel_trmm
    000012d8 g     F .text  000001c0 kernel_gemver
    000005ac g     F .text  000000a4 kernel_floyd_warshall
    00000600 g     F .text  00000160 kernel_symm
    00000538 g     F .text  000000d8 kernel_trisolv
    000005bc g     F .text  0000013c kernel_syr2k
    00000574 g     F .text  00000100 kernel_syrk
    000005f0 g     F .text  00000118 kernel_gemm
     */

    // TODO: fix these addresses!!!!

    lu(0x8dc, 0x8dc + 0x130),
    jacobi1d("jacobi-1d", 0xee4, 0xee4 + 0x184),
    nussinov(0x67c, 0x67c + 0x160),
    ludcmp(0x17c8, 0x17c8 + 0x1fc),
    mvt(0x5d8, 0x5d8 + 0xd4),
    trmm(0x59c, 0x59c + 0x110),
    gemver(0x12d8, 0x12d8 + 0x1c0),
    floydwarshall("floyd-warshall", 0x5ac, 0x5ac + 0xa4),
    symm(0x600, 0x600 + 0x160),
    trisolv(0x538, 0x538 + 0xd8),
    syr2k(0x5bc, 0x5bc + 0x13c),
    syrk(0x574, 0x574 + 0x100),
    gemm(0x5f0, 0x5f0 + 0x118);

    private String elfName;
    private Number kernelStart;
    private Number kernelStop;

    private MicroBlazePolyBenchMiniInt(String name, Number kernelStart, Number kernelStop) {
        this.elfName = name + ".elf";
        this.kernelStart = kernelStart;
        this.kernelStop = kernelStop;
    }

    private MicroBlazePolyBenchMiniInt(Number kernelStart, Number kernelStop) {
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
