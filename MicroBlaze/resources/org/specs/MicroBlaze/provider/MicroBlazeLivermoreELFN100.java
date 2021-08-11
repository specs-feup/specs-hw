package org.specs.MicroBlaze.provider;

public enum MicroBlazeLivermoreELFN100 implements MicroBlazeZippedELFProvider {

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
    state_frag("state_fragment"),
    tri_diag("tri_diagonal");

    private String functionName;
    private String elfName;

    private MicroBlazeLivermoreELFN100(String functionName) {
        this.functionName = functionName;
        this.elfName = name() + ".elf";
    }

    private MicroBlazeLivermoreELFN100() {
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
