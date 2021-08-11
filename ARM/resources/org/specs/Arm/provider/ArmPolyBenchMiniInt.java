package org.specs.Arm.provider;

public enum ArmPolyBenchMiniInt implements ArmZippedELFProvider {

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

    private ArmPolyBenchMiniInt(String elfname) {
        this.functionName = "kernel_" + name();
        this.elfName = elfname + ".elf";
    }

    private ArmPolyBenchMiniInt() {
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
