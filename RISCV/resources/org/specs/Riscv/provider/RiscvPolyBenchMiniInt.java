package org.specs.Riscv.provider;

public enum RiscvPolyBenchMiniInt implements RiscvZippedELFProvider {

    floydwarshall("floyd-warshall"),
    gemm,
    gemver,
    jacobi1d("jacobi-1d"),
    lu,
    ludcmp,
    mvt,
    nussinov,
    symm,
    syr2k,
    syrk,
    trisolv,
    trmm;

    private String functionName;
    private String elfName;

    private RiscvPolyBenchMiniInt(String elfname) {
        this.functionName = "kernel_" + elfname.replace("-", "_");
        this.elfName = elfname + ".elf";
    }

    private RiscvPolyBenchMiniInt() {
        this.functionName = "kernel_" + name();
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
