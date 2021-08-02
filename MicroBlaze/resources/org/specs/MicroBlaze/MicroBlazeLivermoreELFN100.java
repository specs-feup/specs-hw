package org.specs.MicroBlaze;

public enum MicroBlazeLivermoreELFN100 implements MicroBlazeELFProvider {

    cholesky100("org/specs/MicroBlaze/asm/LivermoreN100/cholesky_N100.elf", 0x4b54, 0x4c44),
    diffpredict100("org/specs/MicroBlaze/asm/LivermoreN100/diffpredict_N100.elf", 0x2ec8, 0x3008),
    // glinearrec100("org/specs/MicroBlaze/asm/LivermoreN100/glinearrec_N100.elf", 0x2dac, 0x5698), // linrecurrence?
    // (there are
    // 2
    // linear
    // recurrence kernels)
    hydro100("org/specs/MicroBlaze/asm/LivermoreN100/hydro_N100.elf", 0x4a84, 0x4b50),
    hydro2d100("org/specs/MicroBlaze/asm/LivermoreN100/hydro2d_N100.elf", 0x4d80, 0x5178),
    hydro2dimpl100("org/specs/MicroBlaze/asm/LivermoreN100/hydro2dimpl_N100.elf", 0x5288, 0x550c),
    innerprod100("org/specs/MicroBlaze/asm/LivermoreN100/innerprod_N100.elf", 0x2e64, 0x2ec4),
    intpredict100("org/specs/MicroBlaze/asm/LivermoreN100/intpredict_N100.elf", 0x3c0c, 0x3d8c),
    linrec100("org/specs/MicroBlaze/asm/LivermoreN100/linrec_N100.elf", 0x3568, 0x3610),
    matmul100("org/specs/MicroBlaze/asm/LivermoreN100/matmul_N100.elf", 0x517c, 0x5284),
    pic1d100("org/specs/MicroBlaze/asm/LivermoreN100/pic1d_N100.elf", 0x31e0, 0x3564),
    pic2d100("org/specs/MicroBlaze/asm/LivermoreN100/pic2d_N100.elf", 0x300c, 0x31dc),
    state_frag100("org/specs/MicroBlaze/asm/LivermoreN100/state_frag_N100.elf", 0x4c48, 0x4d7c),
    tri_diag100("org/specs/MicroBlaze/asm/LivermoreN100/tri_diag_N100.elf", 0x5510, 0x55b0);

    private String fullPath;
    private Number kernelStart;
    private Number kernelStop;

    private MicroBlazeLivermoreELFN100(String fullPath, Number kernelStart, Number kernelStop) {
        this.fullPath = fullPath;
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
    public String getResource() {
        return this.fullPath;
    }
}
