package org.specs.Riscv;

public enum RiscvGccELF implements RiscvELFProvider {

    autocor("org/specs/Riscv/asm/GCC/autocor.elf", null, null);

    private String fullPath;
    private Number kernelStart;
    private Number kernelStop;

    private RiscvGccELF(String fullPath, Number kernelStart, Number kernelStop) {
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
