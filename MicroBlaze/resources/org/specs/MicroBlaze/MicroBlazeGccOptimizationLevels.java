package org.specs.MicroBlaze;

import org.specs.BinaryTranslation.ELFProvider;

public enum MicroBlazeGccOptimizationLevels implements ELFProvider {

    autocor0("org/specs/MicroBlaze/asm/GCC/autocor-O0.elf", null, null),
    autocor1("org/specs/MicroBlaze/asm/GCC/autocor-O1.elf", null, null),
    autocor2("org/specs/MicroBlaze/asm/GCC/autocor-O2.elf", null, null),
    autocor2s("org/specs/MicroBlaze/asm/GCC/autocor-O2-small.elf", null, null),
    autocor3("org/specs/MicroBlaze/asm/GCC/autocor-O3.elf", null, null),
    autocorf("org/specs/MicroBlaze/asm/GCC/autocor-Ofast.elf", null, null),
    computeGradient0("org/specs/MicroBlaze/asm/GCC/computeGradient-O0.elf", null, null),
    dotprod0("org/specs/MicroBlaze/asm/GCC/dotprod-O0.elf", null, null),
    dotprod1("org/specs/MicroBlaze/asm/GCC/dotprod-O1.elf", null, null),
    dotprod2("org/specs/MicroBlaze/asm/GCC/dotprod-O2.elf", null, null),
    dotprod2s("org/specs/MicroBlaze/asm/GCC/dotprod-O2-small.elf", null, null),
    dotprod3("org/specs/MicroBlaze/asm/GCC/dotprod-O3.elf", null, null),
    dotprodf("org/specs/MicroBlaze/asm/GCC/dotprod-Ofast.elf", null, null),
    matmul0("org/specs/MicroBlaze/asm/GCC/matmul-O0.elf", null, null),
    test2("org/specs/MicroBlaze/asm/GCC/test-O2.elf", null, null),
    fir1("org/specs/MicroBlaze/asm/GCC/fir-O1.elf", null, null),
    fir2("org/specs/MicroBlaze/asm/GCC/fir-O2.elf", null, null),
    fir3("org/specs/MicroBlaze/asm/GCC/fir-O3.elf", null, null),
    firparam1("org/specs/MicroBlaze/asm/GCC/fir-param-O1.elf", null, null),
    firparam2("org/specs/MicroBlaze/asm/GCC/fir-param-O2.elf", null, null);

    // private String elfname;
    private String fullPath;
    private Number kernelStart;
    private Number kernelStop;

    private MicroBlazeGccOptimizationLevels(String fullPath, Number kernelStart, Number kernelStop) {
        // this.elfname = name();
        this.fullPath = fullPath;
        this.kernelStart = kernelStart;
        this.kernelStop = kernelStop;
    }

    @Override
    public String getResource() {
        return this.fullPath;
    }

    @Override
    public String asTxtDump() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Number getKernelStart() {
        return this.kernelStart;
    }

    @Override
    public Number getKernelStop() {
        return this.kernelStop;
    }

    @Override
    public String asTraceTxtDump() {
        return this.fullPath.replace(".elf", "_trace.txt");
    }

}
