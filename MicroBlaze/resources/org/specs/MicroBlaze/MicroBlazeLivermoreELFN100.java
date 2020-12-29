package org.specs.MicroBlaze;

import pt.up.fe.specs.util.providers.ResourceProvider;

public enum MicroBlazeLivermoreELFN100 implements ResourceProvider {

    cholesky100("org/specs/MicroBlaze/asm/N100/cholesky.elf", 0x4b54, 0x4c44),
    diffpredict100("org/specs/MicroBlaze/asm/N100/diffpredict.elf", 0x2ec8, 0x3008),
    // glinearrec100("org/specs/MicroBlaze/asm/N100/glinearrec.elf", 0x2dac, 0x5698), // linrecurrence? (there are 2
    // linear
    // recurrence kernels)
    hydro100("org/specs/MicroBlaze/asm/N100/hydro.elf", 0x4a84, 0x4b50),
    hydro2d100("org/specs/MicroBlaze/asm/N100/hydro2d.elf", 0x4d80, 0x5178),
    hydro2dimpl100("org/specs/MicroBlaze/asm/N100/hydro2dimpl.elf", 0x5288, 0x550c),
    innerprod100("org/specs/MicroBlaze/asm/N100/innerprod.elf", 0x2e64, 0x2ec4),
    intpredict100("org/specs/MicroBlaze/asm/N100/intpredict.elf", 0x3c0c, 0x3d8c),
    linrec100("org/specs/MicroBlaze/asm/N100/linrec.elf", 0x55b4, 0x5698),
    matmul100("org/specs/MicroBlaze/asm/N100/matmul.elf", 0x517c, 0x5284),
    pic1d100("org/specs/MicroBlaze/asm/N100/pic1d.elf", 0x31e0, 0x3564),
    pic2d100("org/specs/MicroBlaze/asm/N100/pic2d.elf", 0x300c, 0x31dc),
    state_frag100("org/specs/MicroBlaze/asm/N100/state_frag.elf", 0x4c48, 0x4d7c),
    tri_diag100("org/specs/MicroBlaze/asm/N100/tri_diag.elf", 0x5510, 0x55b0);

    // private String elfname;
    private String fullPath;
    private Number kernelStart;
    private Number kernelStop;

    private MicroBlazeLivermoreELFN100(String fullPath, Number kernelStart, Number kernelStop) {
        this.fullPath = fullPath;
        this.kernelStart = kernelStart;
        this.kernelStop = kernelStop;
    }

    public String asTxtDump() {
        return this.fullPath.replace(".elf", ".txt");
    }

    public Number getKernelStart() {
        return kernelStart;
    }

    public Number getKernelStop() {
        return kernelStop;
    }

    @Override
    public String getResource() {
        return this.fullPath;
    }
}
