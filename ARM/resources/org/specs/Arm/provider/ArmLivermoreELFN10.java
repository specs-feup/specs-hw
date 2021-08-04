package org.specs.Arm.provider;

public enum ArmLivermoreELFN10 implements ArmELFProvider {

    cholesky(0x2a20, 0x2ab4),
    diffpredict(0x1500, 0x15bc),
    // glinearrec("org/specs/Arm/asm/N10/glinearrec_N10.elf", 0x3100, 0x31dc),
    // linrecurrence? (there are 2 linear recurrence kernels); this one doesnt work?
    hydro(0x2990, 0x2a14), // 0x2a1c),
    hydro2d(0x2b90, 0x2e9c),
    hydro2dimpl(0x2f68, 0x3074),
    innerprod(0x1488, 0x14f4), // 0x14fc),
    intpredict(0x1e30, 0x1f7c),
    linrec(0x18f0, 0x19a4),
    matmul(0x2ea0, 0x2f64),
    pic1d(0x1730, 0x18ec),
    pic2d(0x15c0, 0x172c),
    state_frag(0x2ab8, 0x2b8c),
    tri_diag(0x3078, 0x30fc);

    private String elfName;
    private Number kernelStart;
    private Number kernelStop;

    private ArmLivermoreELFN10(Number kernelStart, Number kernelStop) {
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
}
