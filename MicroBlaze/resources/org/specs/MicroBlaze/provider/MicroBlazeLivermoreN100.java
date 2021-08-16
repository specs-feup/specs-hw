package org.specs.MicroBlaze.provider;

public enum MicroBlazeLivermoreN100 implements MicroBlazeZippedELFProvider {

    cholesky(),
    diffpredict("difference_predictors"),
    // glinearrec100("org/specs/MicroBlaze/asm/LivermoreN100/glinearrec.elf", 0x2dac, 0x5698), // linrecurrence?
    // (there are
    // 2
    // linear
    // recurrence kernels)
    hydro("hydro_fragment"),
    hydro2d("hydro_2d"),
    hydro2dimpl("hydro_2d_implicit"),
    innerprod("inner_product"),
    intpredict("integrate_predictors"),
    linrec("lin_recurrence"),
    matmul(),
    pic1d("pic_1d"),
    pic2d("pic_2d"),
    statefrag("state_fragment"),
    tridiag("tri_diagonal");

    private String functionName;
    private String elfName;

    private MicroBlazeLivermoreN100(String functionName) {
        this.functionName = functionName;
        this.elfName = name() + ".elf";
    }

    private MicroBlazeLivermoreN100() {
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
