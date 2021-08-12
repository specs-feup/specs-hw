package org.specs.Riscv.provider;

public enum RiscvGccELF implements RiscvZippedELFProvider {

    autocor;

    private String functionName;
    private String elfName;

    private RiscvGccELF() {
        this.functionName = name();
        this.elfName = name() + ".elf";
    }

    @Override
    public String getELFName() {
        return this.elfName;
    }

    @Override
    public String getFunctionName() {
        return this.functionName;
    }
}
