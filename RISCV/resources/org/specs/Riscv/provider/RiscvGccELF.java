package org.specs.Riscv.provider;

public enum RiscvGccELF implements RiscvELFProvider {

    autocor("org/specs/Riscv/asm/GCC/autocor.elf", null, null);

    private String elfName;
    private Number kernelStart;
    private Number kernelStop;

    private RiscvGccELF(String elfName, Number kernelStart, Number kernelStop) {
        this.elfName = elfName;
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
