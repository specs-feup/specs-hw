package org.specs.Arm;

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
    floydwarshall("org/specs/ARM/asm/PolybenchSmallInt/floydwarshall.elf", 0x00000ce4L, 0x0D64L),
    gemm("org/specs/ARM/asm/PolybenchSmallInt/gemm.elf", 0x00000d00L, 0x0DC4L),
    gemver("org/specs/ARM/asm/PolybenchSmallInt/gemver.elf", 0x00000cd0L, 0x0DF8L),
    jacobi1d("org/specs/ARM/asm/PolybenchSmallInt/jacobi1d.elf", 0x00000c40L, 0x0CECL),
    lu("org/specs/ARM/asm/PolybenchSmallInt/lu.elf", 0x00000dd0L, 0x0ED8L),
    ludcmp("org/specs/ARM/asm/PolybenchSmallInt/ludcmp.elf", 0x00000e00L, 0x0FC0L),
    mvt("org/specs/ARM/asm/PolybenchSmallInt/mvt.elf", 0x00000cb0L, 0x0D3CL),
    nussinov("org/specs/ARM/asm/PolybenchSmallInt/nussinov.elf", 0x00000c80L, 0x0DE4L),
    symm("org/specs/ARM/asm/PolybenchSmallInt/symm.elf", 0x00000d20L, 0x0E00L),
    syr2k("org/specs/ARM/asm/PolybenchSmallInt/syr2k.elf", 0x00000cd0L, 0x0DA4L),
    syrk("org/specs/ARM/asm/PolybenchSmallInt/syrk.elf", 0x00000cb0L, 0x0D60L),
    trisolv("org/specs/ARM/asm/PolybenchSmallInt/trisolv.elf", 0x00000c70L, 0x0D08L),
    trmm("org/specs/ARM/asm/PolybenchSmallInt/trmm.elf", 0x00000cc0L, 0x0DC4L);

    private String fullPath;
    private Number kernelStart;
    private Number kernelStop;

    private ArmPolyBenchSmallInt(String fullPath, Number kernelStart, Number kernelStop) {
        this.fullPath = fullPath;
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
    public String getResource() {
        return this.fullPath;
    }
}
