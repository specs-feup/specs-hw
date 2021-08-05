package org.specs.Arm.provider;

public enum ArmPolyBenchSmallFloat implements ArmELFProvider {

    /*
     * output of aarch64-none-elf-objdump -t:
     * 
    0000000000000de0 g     F .text  000000000000010c kernel_lu
    0000000000000ca0 g     F .text  00000000000000d8 kernel_atax
    0000000000000de0 g     F .text  000000000000011c kernel_cholesky
    0000000000000c80 g     F .text  00000000000003b4 kernel_deriche
    0000000000000c44 g     F .text  00000000000000b4 kernel_jacobi_1d
    0000000000000c80 g     F .text  0000000000000188 kernel_nussinov
    0000000000000cb0 g     F .text  00000000000000c0 kernel_bicg
    0000000000000c30 g     F .text  0000000000000138 kernel_durbin
    0000000000000e10 g     F .text  00000000000001c4 kernel_ludcmp
    0000000000000c74 g     F .text  0000000000000130 kernel_jacobi_2d
    0000000000000c70 g     F .text  00000000000002ac kernel_correlation
    0000000000000cc0 g     F .text  0000000000000090 kernel_mvt
    0000000000000cc4 g     F .text  0000000000000104 kernel_trmm
    0000000000000cd0 g     F .text  000000000000010c kernel_gemver
    0000000000000c54 g     F .text  00000000000000e4 kernel_seidel_2d
    0000000000000c50 g     F .text  00000000000002d4 kernel_adi
    0000000000000ce0 g     F .text  00000000000001ac kernel_gramschmidt
    0000000000000da0 g     F .text  0000000000000140 kernel_2mm
    0000000000000c50 g     F .text  000000000000018c kernel_covariance
    0000000000000cf0 g     F .text  00000000000000e0 kernel_doitgen
    0000000000000cc0 g     F .text  00000000000001b8 kernel_fdtd_2d
    0000000000000cf4 g     F .text  0000000000000084 kernel_floyd_warshall
    0000000000000d40 g     F .text  00000000000000e0 kernel_symm
    0000000000000c84 g     F .text  0000000000000084 kernel_trisolv
    0000000000000cf4 g     F .text  00000000000000dc kernel_syr2k
    0000000000000db0 g     F .text  00000000000001d8 kernel_3mm
    0000000000000ca4 g     F .text  0000000000000078 kernel_gesummv
    0000000000000cd0 g     F .text  00000000000000b4 kernel_syrk
    0000000000000c90 g     F .text  00000000000001e0 kernel_heat_3d
    0000000000000d34 g     F .text  00000000000000c4 kernel_gemm
     */
    _2mm(0x00000da0L, 0x0EDCL),
    _3mm(0x00000db0L, 0x0F84L),
    adi(0x00000c50L, 0x0F20L),
    atax(0x00000ca0L, 0x0D74L),
    bicg(0x00000cb0L, 0x0D6CL),
    cholesky(0x00000de0L, 0x0EF8L),
    correlation(0x00000c70L, 0x0F18L),
    covariance(0x00000c50L, 0x0DD8L),
    deriche(0x00000c80L, 0x01030L),
    doitgen(0x00000cf0L, 0x0DCCL),
    durbin(0x00000c30L, 0x0D64L),
    fdtd2d(0x00000cc0L, 0x0E74L),
    floydwarshall(0x00000cf4L, 0x0D74L),
    gemm(0x00000d34L, 0x0DF4L),
    gemver(0x00000cd0L, 0x0DD8L),
    gesummv(0x00000ca4L, 0x0D18L),
    gramschmidt(0x00000ce0L, 0x0E88L),
    heat3d(0x00000c90L, 0x0E6CL),
    jacobi1d(0x00000c44L, 0x0CF4L),
    jacobi2d(0x00000c74L, 0x0DA0L),
    lu(0x00000de0L, 0x0EE8L),
    ludcmp(0x00000e10L, 0x0FD0L),
    mvt(0x00000cc0L, 0x0D4CL),
    nussinov(0x00000c80L, 0x0E04L),
    seidel2d(0x00000c54L, 0x0D34L),
    symm(0x00000d40L, 0x0E1CL),
    syr2k(0x00000cf4L, 0x0DCCL),
    syrk(0x00000cd0L, 0x0D80L),
    trisolv(0x00000c84L, 0x0D04L),
    trmm(0x00000cc4L, 0x0DC4L);

    private String elfName;
    private Number kernelStart;
    private Number kernelStop;

    private ArmPolyBenchSmallFloat(Number kernelStart, Number kernelStop) {
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
