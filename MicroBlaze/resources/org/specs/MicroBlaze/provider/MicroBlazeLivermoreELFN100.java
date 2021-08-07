package org.specs.MicroBlaze.provider;

public enum MicroBlazeLivermoreELFN100 implements MicroBlazeELFProvider {

    cholesky(0x4b54, 0x4c44),
    diffpredict("difference_predictors", 0x2ec8, 0x3008),
    // glinearrec100("org/specs/MicroBlaze/asm/LivermoreN100/glinearrec.elf", 0x2dac, 0x5698), // linrecurrence?
    // (there are
    // 2
    // linear
    // recurrence kernels)
    hydro(0x4a84, 0x4b50),
    hydro2d(0x4d80, 0x5178),
    hydro2dimpl(0x5288, 0x550c),
    innerprod(0x2e64, 0x2ec4),
    intpredict(0x3c0c, 0x3d8c),
    linrec(0x3568, 0x3610),
    matmul(0x517c, 0x5284),
    pic1d(0x31e0, 0x3564),
    pic2d(0x300c, 0x31dc),
    state_frag(0x4c48, 0x4d7c),
    tri_diag(0x5510, 0x55b0);

    private String functionName;
    private String elfName;
    private Number kernelStart;
    private Number kernelStop;

    private MicroBlazeLivermoreELFN100(String functionName, Number kernelStart, Number kernelStop) {
        this.functionName = functionName;
        this.elfName = name() + ".elf";
        this.kernelStart = kernelStart;
        this.kernelStop = kernelStop;
    }

    private MicroBlazeLivermoreELFN100(Number kernelStart, Number kernelStop) {
        this.functionName = name();
        this.elfName = name() + ".elf";
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

    @Override
    public String getFunctionName() {
        return this.functionName;
    }
}
