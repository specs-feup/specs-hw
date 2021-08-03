package org.specs.MicroBlaze.provider;

public enum MicroBlazeGccOptimizationLevels implements MicroBlazeELFProvider {

    autocor0("autocor-O0", null, null),
    autocor1("autocor-O1", null, null),
    autocor2("autocor-O2", null, null),
    autocor2s("autocor-O2-small", null, null),
    autocor3("autocor-O3", null, null),
    autocorf("autocor-Ofast", null, null),
    computeGradient0("computeGradient-O0", null, null),
    dotprod0("dotprod-O0", null, null),
    dotprod1("dotprod-O1", null, null),
    dotprod2("dotprod-O2", null, null),
    dotprod2s("dotprod-O2-small", null, null),
    dotprod3("dotprod-O3", null, null),
    dotprodf("dotprod-Ofast", null, null),
    matmul0("matmul-O0", null, null),
    test2("test-O2", null, null),
    fir1("fir-O1", null, null),
    fir2("fir-O2", null, null),
    fir3("fir-O3", null, null),
    firparam1("fir-param-O1", null, null),
    firparam2("fir-param-O2", null, null);

    private String elfName;
    private Number kernelStart;
    private Number kernelStop;

    private MicroBlazeGccOptimizationLevels(String elfname, Number kernelStart, Number kernelStop) {
        this.elfName = elfname + ".elf";
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
}
