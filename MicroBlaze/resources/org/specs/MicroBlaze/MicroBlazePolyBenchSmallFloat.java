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
    _2mm("org/specs/MicroBlaze/asm/PolybenchSmallFloat/2mm.elf", 0x070c, 0x0890),
    _3mm("org/specs/MicroBlaze/asm/PolybenchSmallFloat/3mm.elf", 0x071c, 0x0930),
    adi("org/specs/MicroBlaze/asm/PolybenchSmallFloat/adi.elf", 0x053c, 0x08e0),
    atax("org/specs/MicroBlaze/asm/PolybenchSmallFloat/atax.elf", 0x05e8, 0x06cc),
    bicg("org/specs/MicroBlaze/asm/PolybenchSmallFloat/bicg.elf", 0x0600, 0x06ec),
    cholesky("org/specs/MicroBlaze/asm/PolybenchSmallFloat/2mm.elf", 0x140c, 0x1558),
    correlation("org/specs/MicroBlaze/asm/PolybenchSmallFloat/2mm.elf", 0x1228, 0x150c),
    covariance("org/specs/MicroBlaze/asm/PolybenchSmallFloat/2mm.elf", 0x053c, 0x06c8),
    deriche("org/specs/MicroBlaze/asm/PolybenchSmallFloat/2mm.elf", 0x2fe4, 0x344c),
    doitgen("org/specs/MicroBlaze/asm/PolybenchSmallFloat/2mm.elf", 0x0648, 0x075c),
    durbin("org/specs/MicroBlaze/asm/PolybenchSmallFloat/2mm.elf", 0x053c, 0x0684),
    fdtd2d("org/specs/MicroBlaze/asm/PolybenchSmallFloat/2mm.elf", 0x0640, 0x0858),
    floydwarshall("org/specs/MicroBlaze/asm/PolybenchSmallFloat/2mm.elf", 0x062c, 0x06cc),
    gemm("org/specs/MicroBlaze/asm/PolybenchSmallFloat/2mm.elf", 0x0694, 0x07a4),
    gemver("org/specs/MicroBlaze/asm/PolybenchSmallFloat/2mm.elf", 0x06b4, 0x0870),
    gesummv("org/specs/MicroBlaze/asm/PolybenchSmallFloat/2mm.elf", 0x0624, 0x0724),
    gramschmidt("org/specs/MicroBlaze/asm/PolybenchSmallFloat/2mm.elf", 0x131c, 0x1514),
    heat3d("org/specs/MicroBlaze/asm/PolybenchSmallFloat/2mm.elf", 0x05f4, 0x0880),
    jacobi1d("org/specs/MicroBlaze/asm/PolybenchSmallFloat/2mm.elf", 0x1120, 0x12a0),
    jacobi2d("org/specs/MicroBlaze/asm/PolybenchSmallFloat/2mm.elf", 0x05f4, 0x0770),
    lu("org/specs/MicroBlaze/asm/PolybenchSmallFloat/2mm.elf", 0x0720, 0x084c),
    ludcmp("org/specs/MicroBlaze/asm/PolybenchSmallFloat/2mm.elf", 0x1900, 0x1af8),
    mvt("org/specs/MicroBlaze/asm/PolybenchSmallFloat/2mm.elf", 0x066c, 0x073c),
    nussinov("org/specs/MicroBlaze/asm/PolybenchSmallFloat/2mm.elf", 0x053c, 0x06a4),
    seidel2d("org/specs/MicroBlaze/asm/PolybenchSmallFloat/2mm.elf", 0x053c, 0x065c),
    symm("org/specs/MicroBlaze/asm/PolybenchSmallFloat/2mm.elf", 0x06a0, 0x065c),
    syr2k("org/specs/MicroBlaze/asm/PolybenchSmallFloat/2mm.elf", 0x0664, 0x077c),
    syrk("org/specs/MicroBlaze/asm/PolybenchSmallFloat/2mm.elf", 0x0620, 0x0714),
    trisolv("org/specs/MicroBlaze/asm/PolybenchSmallFloat/2mm.elf", 0x05cc, 0x06a0),
    trmm("org/specs/MicroBlaze/asm/PolybenchSmallFloat/2mm.elf", 0x0638, 0x0744);

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
