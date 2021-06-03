package org.specs.MicroBlaze;

import org.specs.BinaryTranslation.ELFProvider;

public enum MicroBlazePolyBenchBLASSmall implements ELFProvider {

    /**
     * NOTE: this set of kernels was compiled by cutting out the data init loops, since they take a lot of time, so all
     * the kernel data is not initted, but since kernels are not data dependent (non structured data) it shouldnt matter
     * in terms of segment detection or kernel execution times
     */

    gemm("org/specs/MicroBlaze/asm/PolybenchBLAS_Small/gemm.elf", 0x09b0, 0x0aa8),
    gemver("org/specs/MicroBlaze/asm/PolybenchBLAS_Small/gemver.elf", 0x0ab8, 0x0c60),
    gesummv("org/specs/MicroBlaze/asm/PolybenchBLAS_Small/gesummv.elf", 0x09b0, 0x0a9c),
    symm("org/specs/MicroBlaze/asm/PolybenchBLAS_Small/symm.elf", 0x09b0, 0x0ae4),
    syrk("org/specs/MicroBlaze/asm/PolybenchBLAS_Small/syrk.elf", 0x09b0, 0x0a90),
    syrk2("org/specs/MicroBlaze/asm/PolybenchBLAS_Small/syrk2.elf", 0x09b0, 0x0ab4),
    trmm("org/specs/MicroBlaze/asm/PolybenchBLAS_Small/trmm.elf", 0x09b0, 0x0ab8);

    private String fullPath;
    private Number kernelStart;
    private Number kernelStop;

    private MicroBlazePolyBenchBLASSmall(String fullPath, Number kernelStart, Number kernelStop) {
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
