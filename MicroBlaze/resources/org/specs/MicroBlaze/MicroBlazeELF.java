package org.specs.MicroBlaze;

import pt.up.fe.specs.util.providers.ResourceProvider;

/**
 * Handy resource list of existing pre-compiled ELFs for MicroBlaze, for robust name and path getting (and other things)
 * 
 * @author nuno
 *
 */
public enum MicroBlazeELF implements ResourceProvider {

    cholesky("org/specs/MicroBlaze/asm/cholesky.elf", 0x4b54, 0x4c44),
    diffpredict("org/specs/MicroBlaze/asm/diffpredict.elf", 0x2ec8, 0x3008),
    // glinearrec("org/specs/MicroBlaze/asm/glinearrec.elf", 0x2dac, 0x5698), // linrecurrence? (there are 2 linear
    // recurrence kernels)
    hydro("org/specs/MicroBlaze/asm/hydro.elf", 0x4a84, 0x4b50),
    hydro2d("org/specs/MicroBlaze/asm/hydro2d.elf", 0x4d80, 0x5178),
    innerprod("org/specs/MicroBlaze/asm/innerprod.elf", 0x2e64, 0x2ec4),
    matmul("org/specs/MicroBlaze/asm/matmul.elf", 0x517c, 0x5284),
    pic1d("org/specs/MicroBlaze/asm/pic1d.elf", 0x31e0, 0x3564);

    // private String elfname;
    private String fullPath;
    private Number kernelStart;
    private Number kernelStop;

    private MicroBlazeELF(String fullPath, Number kernelStart, Number kernelStop) {
        // this.elfname = name();
        this.fullPath = fullPath;
        this.kernelStart = kernelStart;
        this.kernelStop = kernelStop;
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
