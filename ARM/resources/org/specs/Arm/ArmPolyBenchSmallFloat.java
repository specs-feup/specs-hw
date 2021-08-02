package org.specs.Arm;

import pt.up.fe.specs.binarytranslation.ELFProvider;

public enum ArmPolyBenchSmallFloat implements ELFProvider {

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
    _2mm("org/specs/ARM/asm/PolybenchSmallFloat/_2mm.elf", 0x00000da0L, 0x0EDCL),
    _3mm("org/specs/ARM/asm/PolybenchSmallFloat/_3mm.elf", 0x00000db0L, 0x0F84L),
    adi("org/specs/ARM/asm/PolybenchSmallFloat/adi.elf", 0x00000c50L, 0x0F20L),
    atax("org/specs/ARM/asm/PolybenchSmallFloat/atax.elf", 0x00000ca0L, 0x0D74L),
    bicg("org/specs/ARM/asm/PolybenchSmallFloat/bicg.elf", 0x00000cb0L, 0x0D6CL),
    cholesky("org/specs/ARM/asm/PolybenchSmallFloat/cholesky.elf", 0x00000de0L, 0x0EF8L),
    correlation("org/specs/ARM/asm/PolybenchSmallFloat/correlation.elf", 0x00000c70L, 0x0F18L),
    covariance("org/specs/ARM/asm/PolybenchSmallFloat/covariance.elf", 0x00000c50L, 0x0DD8L),
    deriche("org/specs/ARM/asm/PolybenchSmallFloat/deriche.elf", 0x00000c80L, 0x01030L),
    doitgen("org/specs/ARM/asm/PolybenchSmallFloat/doitgen.elf", 0x00000cf0L, 0x0DCCL),
    durbin("org/specs/ARM/asm/PolybenchSmallFloat/durbin.elf", 0x00000c30L, 0x0D64L),
    fdtd2d("org/specs/ARM/asm/PolybenchSmallFloat/fdtd2d.elf", 0x00000cc0L, 0x0E74L),
    floydwarshall("org/specs/ARM/asm/PolybenchSmallFloat/floydwarshall .elf", 0x00000cf4L, 0x0D74L),
    gemm("org/specs/ARM/asm/PolybenchSmallFloat/gemm.elf", 0x00000d34L, 0x0DF4L),
    gemver("org/specs/ARM/asm/PolybenchSmallFloat/gemver.elf", 0x00000cd0L, 0x0DD8L),
    gesummv("org/specs/ARM/asm/PolybenchSmallFloat/gesummv.elf", 0x00000ca4L, 0x0D18L),
    gramschmidt("org/specs/ARM/asm/PolybenchSmallFloat/gramschmidt.elf", 0x00000ce0L, 0x0E88L),
    heat3d("org/specs/ARM/asm/PolybenchSmallFloat/heat3d .elf", 0x00000c90L, 0x0E6CL),
    jacobi1d("org/specs/ARM/asm/PolybenchSmallFloat/jacobi1d .elf", 0x00000c44L, 0x0CF4L),
    jacobi2d("org/specs/ARM/asm/PolybenchSmallFloat/jacobi2d .elf", 0x00000c74L, 0x0DA0L),
    lu("org/specs/ARM/asm/PolybenchSmallFloat/lu.elf", 0x00000de0L, 0x0EE8L),
    ludcmp("org/specs/ARM/asm/PolybenchSmallFloat/ludcmp.elf", 0x00000e10L, 0x0FD0L),
    mvt("org/specs/ARM/asm/PolybenchSmallFloat/mvt.elf", 0x00000cc0L, 0x0D4CL),
    nussinov("org/specs/ARM/asm/PolybenchSmallFloat/nussinov.elf", 0x00000c80L, 0x0E04L),
    seidel2d("org/specs/ARM/asm/PolybenchSmallFloat/seidel2d .elf", 0x00000c54L, 0x0D34L),
    symm("org/specs/ARM/asm/PolybenchSmallFloat/symm.elf", 0x00000d40L, 0x0E1CL),
    syr2k("org/specs/ARM/asm/PolybenchSmallFloat/syr2k.elf", 0x00000cf4L, 0x0DCCL),
    syrk("org/specs/ARM/asm/PolybenchSmallFloat/syrk.elf", 0x00000cd0L, 0x0D80L),
    trisolv("org/specs/ARM/asm/PolybenchSmallFloat/trisolv.elf", 0x00000c84L, 0x0D04L),
    trmm("org/specs/ARM/asm/PolybenchSmallFloat/trmm.elf", 0x00000cc4L, 0x0DC4L);

    private String fullPath;
    private Number kernelStart;
    private Number kernelStop;

    private ArmPolyBenchSmallFloat(String fullPath, Number kernelStart, Number kernelStop) {
        this.fullPath = fullPath;
        this.kernelStart = kernelStart;
        this.kernelStop = kernelStop;
    }

    @Override
    public String asTxtDump() {
        return this.fullPath.replace(".elf", ".txt");
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
    public String getResource() {
        return this.fullPath;
    }

    @Override
    public String asTraceTxtDump() {
        return this.fullPath.replace(".elf", "_trace.txt");
    }
}
