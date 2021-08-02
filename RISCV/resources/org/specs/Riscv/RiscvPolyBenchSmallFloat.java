package org.specs.Riscv;

import pt.up.fe.specs.binarytranslation.ELFProvider;

public enum RiscvPolyBenchSmallFloat implements ELFProvider {

    /*
     * output of riscv32-unknown-elf-objdump -t:
     * 
    80000264 g     F .text  00000100 kernel_lu
    80000170 g     F .text  000000f4 kernel_atax
    80000264 g     F .text  0000012c kernel_cholesky
    8000015c g     F .text  000003f0 kernel_deriche
    800000b4 g     F .text  0000013c kernel_jacobi_1d
    80000134 g     F .text  00000160 kernel_nussinov
    800001b0 g     F .text  000000f0 kernel_bicg
    80000078 g     F .text  0000014c kernel_durbin
    80000340 g     F .text  00000218 kernel_ludcmp
    80000138 g     F .text  00000120 kernel_jacobi_2d
    80000144 g     F .text  000002fc kernel_correlation
    800001f8 g     F .text  00000098 kernel_mvt
    800001a4 g     F .text  000000c0 kernel_trmm
    800002d8 g     F .text  00000130 kernel_gemver
    800000e4 g     F .text  000000b8 kernel_seidel_2d
    80000118 g     F .text  00000310 kernel_adi
    800001b0 g     F .text  000001c8 kernel_gramschmidt
    800002ac g     F .text  00000144 kernel_2mm
    80000138 g     F .text  00000164 kernel_covariance
    800001c8 g     F .text  000000f8 kernel_doitgen
    800001bc g     F .text  000001d0 kernel_fdtd_2d
    8000012c g     F .text  00000080 kernel_floyd_warshall
    80000254 g     F .text  000000f8 kernel_symm
    8000013c g     F .text  0000008c kernel_trisolv
    80000228 g     F .text  00000100 kernel_syr2k
    800002d0 g     F .text  000001bc kernel_3mm
    800001c8 g     F .text  00000098 kernel_gesummv
    800001dc g     F .text  000000ac kernel_syrk
    80000148 g     F .text  00000228 kernel_heat_3d
    80000244 g     F .text  000000ac kernel_gemm
     */
    _2mm("org/specs/RISCV/asm/PolybenchSmallFloat/2mm.elf", 0x800002acL, 0x800003ECL),
    _3mm("org/specs/RISCV/asm/PolybenchSmallFloat/3mm.elf", 0x800002d0L, 0x80000488L),
    adi("org/specs/RISCV/asm/PolybenchSmallFloat/adi.elf", 0x80000118L, 0x80000424L),
    atax("org/specs/RISCV/asm/PolybenchSmallFloat/atax.elf", 0x80000170L, 0x80000260L),
    bicg("org/specs/RISCV/asm/PolybenchSmallFloat/bicg.elf", 0x800001b0L, 0x8000029CL),
    cholesky("org/specs/RISCV/asm/PolybenchSmallFloat/cholesky.elf", 0x80000264L, 0x8000038CL),
    correlation("org/specs/RISCV/asm/PolybenchSmallFloat/correlation.elf", 0x80000144L, 0x8000043CL),
    covariance("org/specs/RISCV/asm/PolybenchSmallFloat/covariance.elf", 0x80000138L, 0x80000298L),
    deriche("org/specs/RISCV/asm/PolybenchSmallFloat/deriche.elf", 0x8000015cL, 0x80000548L),
    doitgen("org/specs/RISCV/asm/PolybenchSmallFloat/doitgen.elf", 0x800001c8L, 0x800002BCL),
    durbin("org/specs/RISCV/asm/PolybenchSmallFloat/durbin.elf", 0x80000078L, 0x800001C0L),
    fdtd2d("org/specs/RISCV/asm/PolybenchSmallFloat/fdtd-2d.elf", 0x800001bcL, 0x80000388L),
    floydwarshall("org/specs/RISCV/asm/PolybenchSmallFloat/floyd-warshall.elf", 0x8000012cL, 0x800001A8L),
    gemm("org/specs/RISCV/asm/PolybenchSmallFloat/gemm.elf", 0x80000244L, 0x800002ECL),
    gemver("org/specs/RISCV/asm/PolybenchSmallFloat/gemver.elf", 0x800002d8L, 0x80000404L),
    gesummv("org/specs/RISCV/asm/PolybenchSmallFloat/gesummv.elf", 0x800001c8L, 0x8000025CL),
    gramschmidt("org/specs/RISCV/asm/PolybenchSmallFloat/gramschmidt.elf", 0x800001b0L, 0x80000374L),
    heat3d("org/specs/RISCV/asm/PolybenchSmallFloat/heat3d .elf", 0x80000148L, 0x8000036CL),
    jacobi1d("org/specs/RISCV/asm/PolybenchSmallFloat/jacobi-1d.elf", 0x800000b4L, 0x800001ECL),
    jacobi2d("org/specs/RISCV/asm/PolybenchSmallFloat/jacobi-2d.elf", 0x80000138L, 0x80000254L),
    lu("org/specs/RISCV/asm/PolybenchSmallFloat/lu.elf", 0x80000264L, 0x80000360L),
    ludcmp("org/specs/RISCV/asm/PolybenchSmallFloat/ludcmp.elf", 0x80000340L, 0x80000554L),
    mvt("org/specs/RISCV/asm/PolybenchSmallFloat/mvt.elf", 0x800001f8L, 0x8000028CL),
    nussinov("org/specs/RISCV/asm/PolybenchSmallFloat/nussinov.elf", 0x80000134L, 0x80000290L),
    seidel2d("org/specs/RISCV/asm/PolybenchSmallFloat/seidel-2d.elf", 0x800000e4L, 0x80000198L),
    symm("org/specs/RISCV/asm/PolybenchSmallFloat/symm.elf", 0x80000254L, 0x80000348L),
    syr2k("org/specs/RISCV/asm/PolybenchSmallFloat/syr2k.elf", 0x80000228L, 0x80000324L),
    syrk("org/specs/RISCV/asm/PolybenchSmallFloat/syrk.elf", 0x800001dcL, 0x80000284L),
    trisolv("org/specs/RISCV/asm/PolybenchSmallFloat/trisolv.elf", 0x8000013cL, 0x800001C4L),
    trmm("org/specs/RISCV/asm/PolybenchSmallFloat/trmm.elf", 0x800001a4L, 0x80000260L);

    private String fullPath;
    private Number kernelStart;
    private Number kernelStop;

    private RiscvPolyBenchSmallFloat(String fullPath, Number kernelStart, Number kernelStop) {
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
