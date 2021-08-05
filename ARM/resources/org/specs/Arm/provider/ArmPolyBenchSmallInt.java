package org.specs.Arm.provider;

public enum ArmPolyBenchSmallInt implements ArmELFProvider {

    /*
     * output of aarch64-none-elf-objdump -t:
     * 
    0000000000000dd0 g     F .text  000000000000010c kernel_lu
    0000000000000c40 g     F .text  00000000000000b0 kernel_jacobi_1d
    0000000000000c80 g     F .text  0000000000000168 kernel_nussinov
    0000000000000e00 g     F .text  00000000000001c4 kernel_ludcmp
    0000000000000cb0 g     F .text  0000000000000090 kernel_mvt
    0000000000000cc0 g     F .text  0000000000000108 kernel_trmm
    0000000000000cd0 g     F .text  000000000000012c kernel_gemver
    0000000000000ce4 g     F .text  0000000000000084 kernel_floyd_warshall
    0000000000000d20 g     F .text  00000000000000e4 kernel_symm
    0000000000000c70 g     F .text  000000000000009c kernel_trisolv
    0000000000000cd0 g     F .text  00000000000000d8 kernel_syr2k
    0000000000000cb0 g     F .text  00000000000000b4 kernel_syrk
    0000000000000d00 g     F .text  00000000000000c8 kernel_gemm
     */
    floydwarshall(0x00000ce4L, 0x0D64L),
    gemm(0x00000d00L, 0x0DC4L),
    gemver(0x00000cd0L, 0x0DF8L),
    jacobi1d(0x00000c40L, 0x0CECL),
    lu(0x00000dd0L, 0x0ED8L),
    ludcmp(0x00000e00L, 0x0FC0L),
    mvt(0x00000cb0L, 0x0D3CL),
    nussinov(0x00000c80L, 0x0DE4L),
    symm(0x00000d20L, 0x0E00L),
    syr2k(0x00000cd0L, 0x0DA4L),
    syrk(0x00000cb0L, 0x0D60L),
    trisolv(0x00000c70L, 0x0D08L),
    trmm(0x00000cc0L, 0x0DC4L);

    private String elfName;
    private Number kernelStart;
    private Number kernelStop;

    private ArmPolyBenchSmallInt(Number kernelStart, Number kernelStop) {
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
