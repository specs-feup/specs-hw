package org.specs.Riscv;

import org.specs.BinaryTranslation.ELFProvider;

public enum RiscvLivermoreELFN100iam implements ELFProvider {

    cholesky100("org/specs/Riscv/asm/N100iam/cholesky_N100.elf", 0x80003778L, 0x8000393cL),
    diffpredict100("org/specs/Riscv/asm/N100iam/diffpredict_N100.elf", 0x80001220L, 0x80001384L),
    // glinearrec100("org/specs/Riscv/asm/N100iam/glinearrec_N100.elf", 0x8000324cL, 0x800033c4L), // linrecurrence?
    // (there
    // are
    // 2
    // linear
    // recurrence kernels)
    hydro100("org/specs/Riscv/asm/N100iam/hydro_N100.elf", 0x800033c8L, 0x80003580L),
    hydro2d100("org/specs/Riscv/asm/N100iam/hydro2d_N100.elf", 0x80003ec8L, 0x80004690L),
    hydro2dimpl100("org/specs/Riscv/asm/N100iam/hydro2dimpl_N100.elf", 0x80003b9cL, 0x80003ec4L),
    innerprod100("org/specs/Riscv/asm/N100iam/innerprod_N100.elf", 0x80001140L, 0x8000121cL),
    intpredict100("org/specs/Riscv/asm/N100iam/intpredict_N100.elf", 0x800021c0L, 0x800023e4L),
    linrec100("org/specs/Riscv/asm/N100iam/linrec_N100.elf", 0x8000324cL, 0x800033c4L),
    matmul100("org/specs/Riscv/asm/N100iam/matmul_N100.elf", 0x80003584L, 0x80003774L),
    pic1d100("org/specs/Riscv/asm/N100iam/pic1d_N100.elf", 0x80001650L, 0x80001a34L),
    pic2d100("org/specs/Riscv/asm/N100iam/pic2d_N100.elf", 0x80001388L, 0x8000164cL),
    state_frag100("org/specs/Riscv/asm/N100iam/state_frag_N100.elf", 0x80003940L, 0x80003b98L),
    tri_diag100("org/specs/Riscv/asm/N100iam/tri_diag_N100.elf", 0x800030ecL, 0x80003248L);

    private String fullPath;
    private Number kernelStart;
    private Number kernelStop;

    private RiscvLivermoreELFN100iam(String fullPath, Number kernelStart, Number kernelStop) {
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

    public String asTraceTxtDump() {
        // TODO Auto-generated method stub
        return null;
    }
}
