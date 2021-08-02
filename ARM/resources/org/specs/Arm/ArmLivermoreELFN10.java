package org.specs.Arm;

public enum ArmLivermoreELFN10 implements ArmELFProvider {

    cholesky("org/specs/Arm/asm/N10/cholesky_N10.elf", 0x2a20, 0x2ab4),
    diffpredict("org/specs/Arm/asm/N10/diffpredict_N10.elf", 0x1500, 0x15bc),
    // glinearrec("org/specs/Arm/asm/N10/glinearrec_N10.elf", 0x3100, 0x31dc),
    // linrecurrence? (there are 2 linear recurrence kernels); this one doesnt work?
    hydro("org/specs/Arm/asm/N10/hydro_N10.elf", 0x2990, 0x2a14), // 0x2a1c),
    hydro2d("org/specs/Arm/asm/N10/hydro2d_N10.elf", 0x2b90, 0x2e9c),
    hydro2dimpl("org/specs/Arm/asm/N10/hydro2dimpl_N10.elf", 0x2f68, 0x3074),
    innerprod("org/specs/Arm/asm/N10/innerprod_N10.elf", 0x1488, 0x14f4), // 0x14fc),
    intpredict("org/specs/Arm/asm/N10/intpredict_N10.elf", 0x1e30, 0x1f7c),
    linrec("org/specs/Arm/asm/N10/linrec_N10.elf", 0x18f0, 0x19a4),
    matmul("org/specs/Arm/asm/N10/matmul_N10.elf", 0x2ea0, 0x2f64),
    pic1d("org/specs/Arm/asm/N10/pic1d_N10.elf", 0x1730, 0x18ec),
    pic2d("org/specs/Arm/asm/N10/pic2d_N10.elf", 0x15c0, 0x172c),
    state_frag("org/specs/Arm/asm/N10/state_frag_N10.elf", 0x2ab8, 0x2b8c),
    tri_diag("org/specs/Arm/asm/N10/tri_diag_N10.elf", 0x3078, 0x30fc);

    private String fullPath;
    private Number kernelStart;
    private Number kernelStop;

    private ArmLivermoreELFN10(String fullPath, Number kernelStart, Number kernelStop) {
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
