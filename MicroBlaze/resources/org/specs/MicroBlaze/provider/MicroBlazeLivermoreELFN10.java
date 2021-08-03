package org.specs.MicroBlaze.provider;

/**
 * Handy resource list of existing pre-compiled ELFs for MicroBlaze, for robust name and path getting (and other things)
 * 
 * @author nuno
 *
 */
public enum MicroBlazeLivermoreELFN10 implements MicroBlazeELFProvider {

    cholesky("org/specs/MicroBlaze/asm/LivermoreN10/cholesky.elf", 0x4b54, 0x4c44),
    // cholesky_trace("org/specs/MicroBlaze/asm/LivermoreN10/cholesky_trace.txt", null, null),
    diffpredict("org/specs/MicroBlaze/asm/LivermoreN10/diffpredict.elf", 0x2ec8, 0x3008),
    // glinearrec("org/specs/MicroBlaze/asm/LivermoreN10/glinearrec.elf", 0x2dac, 0x5698), // linrecurrence? (there are
    // 2 linear
    // recurrence kernels)
    hydro("org/specs/MicroBlaze/asm/LivermoreN10/hydro.elf", 0x4a84, 0x4b50),
    hydro2d("org/specs/MicroBlaze/asm/LivermoreN10/hydro2d.elf", 0x4d80, 0x5178),
    hydro2dimpl("org/specs/MicroBlaze/asm/LivermoreN10/hydro2dimpl.elf", 0x5288, 0x550c),
    innerprod("org/specs/MicroBlaze/asm/LivermoreN10/innerprod.elf", 0x2e64, 0x2ec4),
    intpredict("org/specs/MicroBlaze/asm/LivermoreN10/intpredict.elf", 0x3c0c, 0x3d8c),
    linrec("org/specs/MicroBlaze/asm/LivermoreN10/linrec.elf", 0x3568, 0x3610),
    matmul("org/specs/MicroBlaze/asm/LivermoreN10/matmul.elf", 0x517c, 0x5284),
    pic1d("org/specs/MicroBlaze/asm/LivermoreN10/pic1d.elf", 0x31e0, 0x3564),
    pic2d("org/specs/MicroBlaze/asm/LivermoreN10/pic2d.elf", 0x300c, 0x31dc),
    state_frag("org/specs/MicroBlaze/asm/LivermoreN10/state_frag.elf", 0x4c48, 0x4d7c),
    tri_diag("org/specs/MicroBlaze/asm/LivermoreN10/tri_diag.elf", 0x5510, 0x55b0);

    private String fullPath;
    private Number kernelStart;
    private Number kernelStop;

    private MicroBlazeLivermoreELFN10(String fullPath, Number kernelStart, Number kernelStop) {
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
