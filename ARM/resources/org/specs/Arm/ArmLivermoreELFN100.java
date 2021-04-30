package org.specs.Arm;

import org.specs.BinaryTranslation.ELFProvider;

public enum ArmLivermoreELFN100 implements ELFProvider {

    cholesky("org/specs/Arm/asm/N100/cholesky_N100.elf", 0x2a20, 0x2ab4),
    diffpredict("org/specs/Arm/asm/N100/diffpredict_N100.elf", 0x1500, 0x15bc),
    // glinearrec("org/specs/Arm/asm/N100/glinearrec_N100.elf", 0x3100, 0x31dc),
    // linrecurrence? (there are 2 linear recurrence kernels); this one doesnt work?
    hydro("org/specs/Arm/asm/N100/hydro_N100.elf", 0x2990, 0x2a14), // 0x2a1c),
    hydro2d("org/specs/Arm/asm/N100/hydro2d_N100.elf", 0x2b90, 0x2e9c),
    hydro2dimpl("org/specs/Arm/asm/N100/hydro2dimpl_N100.elf", 0x2f68, 0x3074),
    innerprod("org/specs/Arm/asm/N100/innerprod_N100.elf", 0x1488, 0x14f4), // 0x14fc),
    intpredict("org/specs/Arm/asm/N100/intpredict_N100.elf", 0x1e30, 0x1f7c),
    linrec("org/specs/Arm/asm/N100/linrec_N100.elf", 0x18f0, 0x19a4),
    matmul("org/specs/Arm/asm/N100/matmul_N100.elf", 0x2ea0, 0x2f64),
    pic1d("org/specs/Arm/asm/N100/pic1d_N100.elf", 0x1730, 0x18ec),
    pic2d("org/specs/Arm/asm/N100/pic2d_N100.elf", 0x15c0, 0x172c),
    state_frag("org/specs/Arm/asm/N100/state_frag_N100.elf", 0x2ab8, 0x2b8c),
    tri_diag("org/specs/Arm/asm/N100/tri_diag_N100.elf", 0x3078, 0x30fc);

    private String fullPath;
    private Number kernelStart;
    private Number kernelStop;

    private ArmLivermoreELFN100(String fullPath, Number kernelStart, Number kernelStop) {
        this.fullPath = fullPath;
        this.kernelStart = kernelStart;
        this.kernelStop = kernelStop;
    }

    @Override
    public String asTxtDump() {
        return this.fullPath.replace(".elf", ".txt");
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

    @Override
    public String asTraceTxtDump() {
        return this.fullPath.replace(".elf", "_trace.txt");
    }

}
