package org.specs.Arm;

import pt.up.fe.specs.util.providers.ResourceProvider;

public enum ArmELF implements ResourceProvider {

    cholesky("org/specs/Arm/asm/cholesky.elf", 0x2a20, 0x2ab4),
    diffpredict("org/specs/Arm/asm/diffpredict.elf", 0x1500, 0x15bc),
    glinearrec("org/specs/Arm/asm/glinearrec.elf", 0x3100, 0x31dc), // linrecurrence? (there are 2 linear
                                                                    // recurrence kernels)
    hydro("org/specs/Arm/asm/hydro.elf", 0x2990, 0x2a1c),
    hydro2d("org/specs/Arm/asm/hydro2d.elf", 0x2b90, 0x2e9c),
    innerprod("org/specs/Arm/asm/innerprod.elf", 0x1488, 0x14fc),
    matmul("org/specs/Arm/asm/matmul.elf", 0x2ea0, 0x2f64);

    // private String elfname;
    private String fullPath;
    private Number kernelStart;
    private Number kernelStop;

    private ArmELF(String fullPath, Number kernelStart, Number kernelStop) {
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
