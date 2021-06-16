package org.specs.MicroBlaze;

import org.specs.BinaryTranslation.ELFProvider;

public enum MicroBlazePolyBenchSmallInt implements ELFProvider {

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
    floydwarshall("org/specs/MicroBlaze/asm/PolybenchSmallFloat/2mm.elf", 0x0624, 0x06c4),
    gemm("org/specs/MicroBlaze/asm/PolybenchSmallFloat/2mm.elf", 0x0668, 0x0778),
    gemver("org/specs/MicroBlaze/asm/PolybenchSmallFloat/2mm.elf", 0x13f4, 0x15b0),
    jacobi1d("org/specs/MicroBlaze/asm/PolybenchSmallFloat/2mm.elf", 0x0fa4, 0x1124),
    lu("org/specs/MicroBlaze/asm/PolybenchSmallFloat/2mm.elf", 0x0710, 0x083c),
    ludcmp("org/specs/MicroBlaze/asm/PolybenchSmallFloat/2mm.elf", 0x1768, 0x1960),
    mvt("org/specs/MicroBlaze/asm/PolybenchSmallFloat/2mm.elf", 0x0650, 0x0720),
    nussinov("org/specs/MicroBlaze/asm/PolybenchSmallFloat/2mm.elf", 0x053c, 0x0698),
    symm("org/specs/MicroBlaze/asm/PolybenchSmallFloat/2mm.elf", 0x0678, 0x07d0),
    syr2k("org/specs/MicroBlaze/asm/PolybenchSmallFloat/2mm.elf", 0x0634, 0x074c),
    syrk("org/specs/MicroBlaze/asm/PolybenchSmallFloat/2mm.elf", 0x05ec, 0x0610),
    trisolv("org/specs/MicroBlaze/asm/PolybenchSmallFloat/2mm.elf", 0x053c, 0x0610),
    trmm("org/specs/MicroBlaze/asm/PolybenchSmallFloat/2mm.elf", 0x0614, 0x0720);

    private String fullPath;
    private Number kernelStart;
    private Number kernelStop;

    private MicroBlazePolyBenchSmallInt(String fullPath, Number kernelStart, Number kernelStop) {
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
