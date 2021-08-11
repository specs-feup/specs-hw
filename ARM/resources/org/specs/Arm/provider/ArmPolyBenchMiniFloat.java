package org.specs.Arm.provider;

public enum ArmPolyBenchMiniFloat implements ArmZippedELFProvider {

    _2mm("2mm"),
    _3mm("3mm"),
    adi,
    atax,
    bicg,
    cholesky,
    correlation,
    covariance,
    deriche,
    doitgen,
    durbin,
    fdtd2d,
    floydwarshall("floyd-warshall"),
    gemm,
    gemver,
    gesummv,
    gramschmidt,
    heat3d,
    jacobi1d("jacobi-1d"),
    jacobi2d("jacobi-2d"),
    lu,
    ludcmp,
    mvt,
    nussinov,
    seidel2d,
    symm,
    syr2k,
    syrk,
    trisolv,
    trmm;

    private String functionName;
    private String elfName;

    private ArmPolyBenchMiniFloat(String elfname) {
        this.functionName = "kernel_" + name();
        this.elfName = elfname + ".elf";
    }

    private ArmPolyBenchMiniFloat() {
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
