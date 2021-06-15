package org.specs.Riscv;

import org.specs.BinaryTranslation.ELFProvider;

public enum Riscv32IMAFPolybench implements ELFProvider {

    gemm("org/specs/Riscv/asm/32IAMFPolybenchSmall/gemm.elf", 0x09b0, 0x0aa8),
    gemver("org/specs/Riscv/asm/32IAMFPolybenchSmall/gemver.elf", 0x0ab8, 0x0c60),
    gesummv("org/specs/Riscv/asm/32IAMFPolybenchSmall/gesummv.elf", 0x09b0, 0x0a9c),
    symm("org/specs/Riscv/asm/32IAMFPolybenchSmall/symm.elf", 0x09b0, 0x0ae4),
    syrk("org/specs/Riscv/asm/32IAMFPolybenchSmall/syrk.elf", 0x09b0, 0x0a90),
    syrk2("org/specs/Riscv/asm/32IAMFPolybenchSmall/syrk2.elf", 0x09b0, 0x0ab4),
    trmm("org/specs/Riscv/asm/32IAMFPolybenchSmall/trmm.elf", 0x09b0, 0x0ab8);

    private String fullPath;
    private Number kernelStart;
    private Number kernelStop;

    private Riscv32IMAFPolybench(String fullPath, Number kernelStart, Number kernelStop) {
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
