package org.specs.MicroBlaze.provider;

public enum MicroBlazePolyBenchMiniInt implements MicroBlazeZippedELFProvider {

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

    private MicroBlazePolyBenchMiniInt(String elfname) {
        this.functionName = "kernel_" + elfname.replace("-", "_");
        this.elfName = elfname + ".elf";
    }

    private MicroBlazePolyBenchMiniInt() {
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
