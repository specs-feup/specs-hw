package org.specs.Riscv.provider;

public enum RiscvPolyBenchMiniFloat implements RiscvZippedELFProvider {

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
    fdtd2d("fdtd-2d"),
    floydwarshall("floyd-warshall"),
    gemm,
    gemver,
    gesummv,
    gramschmidt,
    heat3d("heat-3d"),
    jacobi1d("jacobi-1d"),
    jacobi2d("jacobi-2d"),
    lu,
    ludcmp,
    mvt,
    nussinov,
    seidel2d("seidel-2d"),
    symm,
    syr2k,
    syrk,
    trisolv,
    trmm;

    private String functionName;
    private String elfName;

    private RiscvPolyBenchMiniFloat(String elfname) {
        this.functionName = "kernel_" + elfname.replace("-", "_");
        this.elfName = elfname + ".elf";
    }

    private RiscvPolyBenchMiniFloat() {
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
