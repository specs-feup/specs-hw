package org.specs.MicroBlaze;

import org.specs.BinaryTranslation.ELFProvider;

public enum MicroBlazePolyBenchSmallFloat implements ELFProvider {

    /*
     * output of mb-objdump -t:
     * 
     * 00000720 g     F .text   00000130 kernel_lu
        000005e8 g     F .text  000000e8 kernel_atax
        0000140c g     F .text  00000180 kernel_cholesky
        00002fe4 g     F .text  0000046c kernel_deriche
        00001120 g     F .text  00000184 kernel_jacobi_1d
        0000053c g     F .text  0000016c kernel_nussinov
        00000600 g     F .text  000000f0 kernel_bicg
        0000053c g     F .text  0000014c kernel_durbin
        00001900 g     F .text  000001fc kernel_ludcmp
        000005f4 g     F .text  00000180 kernel_jacobi_2d
        00001228 g     F .text  0000036c kernel_correlation
        0000066c g     F .text  000000d4 kernel_mvt
        00000638 g     F .text  00000110 kernel_trmm
        000006b4 g     F .text  000001c0 kernel_gemver
        0000053c g     F .text  00000124 kernel_seidel_2d
        0000053c g     F .text  000003a8 kernel_adi
        0000131c g     F .text  000001fc kernel_gramschmidt
        0000070c g     F .text  00000188 kernel_2mm
        0000053c g     F .text  000001cc kernel_covariance
        00000648 g     F .text  00000118 kernel_doitgen
        00000640 g     F .text  0000021c kernel_fdtd_2d
        0000062c g     F .text  000000a4 kernel_floyd_warshall
        000006a0 g     F .text  00000150 kernel_symm
        000005cc g     F .text  000000d8 kernel_trisolv
        00000664 g     F .text  0000011c kernel_syr2k
        0000071c g     F .text  00000218 kernel_3mm
        00000624 g     F .text  00000104 kernel_gesummv
        00000620 g     F .text  000000f8 kernel_syrk
        000005f4 g     F .text  00000290 kernel_heat_3d
        00000694 g     F .text  00000114 kernel_gemm
     */
    _2mm("org/specs/MicroBlaze/asm/PolybenchSmallFloat/_2mm.elf", 0x070cL, 0x890L),
    _3mm("org/specs/MicroBlaze/asm/PolybenchSmallFloat/_3mm.elf", 0x071cL, 0x930L),
    adi("org/specs/MicroBlaze/asm/PolybenchSmallFloat/adi.elf", 0x053cL, 0x8E0L),
    atax("org/specs/MicroBlaze/asm/PolybenchSmallFloat/atax.elf", 0x05e8L, 0x6CCL),
    bicg("org/specs/MicroBlaze/asm/PolybenchSmallFloat/bicg.elf", 0x0600L, 0x6ECL),
    cholesky("org/specs/MicroBlaze/asm/PolybenchSmallFloat/cholesky.elf", 0x140cL, 0x1588L),
    correlation("org/specs/MicroBlaze/asm/PolybenchSmallFloat/correlation.elf", 0x1228L, 0x1590L),
    covariance("org/specs/MicroBlaze/asm/PolybenchSmallFloat/covariance.elf", 0x053cL, 0x704L),
    deriche("org/specs/MicroBlaze/asm/PolybenchSmallFloat/deriche.elf", 0x2fe4L, 0x344CL),
    doitgen("org/specs/MicroBlaze/asm/PolybenchSmallFloat/doitgen.elf", 0x0648L, 0x75CL),
    durbin("org/specs/MicroBlaze/asm/PolybenchSmallFloat/durbin.elf", 0x053cL, 0x684L),
    fdtd2d("org/specs/MicroBlaze/asm/PolybenchSmallFloat/fdtd2d.elf", 0x0640L, 0x858L),
    floydwarshall("org/specs/MicroBlaze/asm/PolybenchSmallFloat/floydwarshall .elf", 0x062cL, 0x6CCL),
    gemm("org/specs/MicroBlaze/asm/PolybenchSmallFloat/gemm.elf", 0x0694L, 0x7A4L),
    gemver("org/specs/MicroBlaze/asm/PolybenchSmallFloat/gemver.elf", 0x06b4L, 0x870L),
    gesummv("org/specs/MicroBlaze/asm/PolybenchSmallFloat/gesummv.elf", 0x0624L, 0x724L),
    gramschmidt("org/specs/MicroBlaze/asm/PolybenchSmallFloat/gramschmidt.elf", 0x131cL, 0x1514L),
    heat3d("org/specs/MicroBlaze/asm/PolybenchSmallFloat/heat3d .elf", 0x05f4L, 0x880L),
    jacobi1d("org/specs/MicroBlaze/asm/PolybenchSmallFloat/jacobi1d .elf", 0x1120L, 0x12A0L),
    jacobi2d("org/specs/MicroBlaze/asm/PolybenchSmallFloat/jacobi2d .elf", 0x05f4L, 0x770L),
    lu("org/specs/MicroBlaze/asm/PolybenchSmallFloat/lu.elf", 0x0720L, 0x84CL),
    ludcmp("org/specs/MicroBlaze/asm/PolybenchSmallFloat/ludcmp.elf", 0x1900L, 0x1AF8L),
    mvt("org/specs/MicroBlaze/asm/PolybenchSmallFloat/mvt.elf", 0x066cL, 0x73CL),
    nussinov("org/specs/MicroBlaze/asm/PolybenchSmallFloat/nussinov.elf", 0x053cL, 0x6A4L),
    seidel2d("org/specs/MicroBlaze/asm/PolybenchSmallFloat/seidel2d .elf", 0x053cL, 0x65CL),
    symm("org/specs/MicroBlaze/asm/PolybenchSmallFloat/symm.elf", 0x06a0L, 0x7ECL),
    syr2k("org/specs/MicroBlaze/asm/PolybenchSmallFloat/syr2k.elf", 0x0664L, 0x77CL),
    syrk("org/specs/MicroBlaze/asm/PolybenchSmallFloat/syrk.elf", 0x0620L, 0x714L),
    trisolv("org/specs/MicroBlaze/asm/PolybenchSmallFloat/trisolv.elf", 0x05ccL, 0x6A0L),
    trmm("org/specs/MicroBlaze/asm/PolybenchSmallFloat/trmm.elf", 0x0638L, 0x744L);

    private String fullPath;
    private Number kernelStart;
    private Number kernelStop;

    private MicroBlazePolyBenchSmallFloat(String fullPath, Number kernelStart, Number kernelStop) {
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
