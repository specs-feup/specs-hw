package org.specs.MicroBlaze;

import org.specs.BinaryTranslation.ELFProvider;

public enum MicroBlazePolyBenchBLAS implements ELFProvider {

    gemm("org/specs/MicroBlaze/asm/PolybenchBLAS/gemm.elf", 0x09b0, 0x0aa8),
    gemver("org/specs/MicroBlaze/asm/PolybenchBLAS/gemver.elf", 0x0ab8, 0x0c60),
    gesummv("org/specs/MicroBlaze/asm/PolybenchBLAS/gesummv.elf", 0x09b0, 0x0a9c),
    symm("org/specs/MicroBlaze/asm/PolybenchBLAS/symm.elf", 0x09b0, 0x0ae4),
    syrk("org/specs/MicroBlaze/asm/PolybenchBLAS/syrk.elf", 0x09b0, 0x0a90),
    syrk2("org/specs/MicroBlaze/asm/PolybenchBLAS/syrk2.elf", 0x09b0, 0x0ab4),
    trmm("org/specs/MicroBlaze/asm/PolybenchBLAS/trmm.elf", 0x09b0, 0x0ab8);

    private String fullPath;
    private Number kernelStart;
    private Number kernelStop;

    private MicroBlazePolyBenchBLAS(String fullPath, Number kernelStart, Number kernelStop) {
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
}