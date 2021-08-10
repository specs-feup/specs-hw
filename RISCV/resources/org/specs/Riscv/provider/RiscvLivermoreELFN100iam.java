package org.specs.Riscv.provider;

public enum RiscvLivermoreELFN100iam implements RiscvELFProvider {

    cholesky("org/specs/Riscv/asm/N100iam/cholesky_N100.elf", 0x80003778L, 0x8000393cL),
    diffpredict("org/specs/Riscv/asm/N100iam/diffpredict_N100.elf", 0x80001220L, 0x80001384L),
    // glinearrec("org/specs/Riscv/asm/N100iam/glinearrec_N100.elf", 0x8000324cL, 0x800033c4L), // linrecurrence?
    // (there
    // are
    // 2
    // linear
    // recurrence kernels)
    hydro("org/specs/Riscv/asm/N100iam/hydro_N100.elf", 0x800033c8L, 0x80003580L),
    hydro2d("org/specs/Riscv/asm/N100iam/hydro2d_N100.elf", 0x80003ec8L, 0x80004690L),
    hydro2dimpl("org/specs/Riscv/asm/N100iam/hydro2dimpl_N100.elf", 0x80003b9cL, 0x80003ec4L),
    innerprod("org/specs/Riscv/asm/N100iam/innerprod_N100.elf", 0x80001140L, 0x8000121cL),
    intpredict("org/specs/Riscv/asm/N100iam/intpredict_N100.elf", 0x800021c0L, 0x800023e4L),
    linrec("org/specs/Riscv/asm/N100iam/linrec_N100.elf", 0x8000324cL, 0x800033c4L),
    matmul("org/specs/Riscv/asm/N100iam/matmul_N100.elf", 0x80003584L, 0x80003774L),
    pic1d("org/specs/Riscv/asm/N100iam/pic1d_N100.elf", 0x80001650L, 0x80001a34L),
    pic2d("org/specs/Riscv/asm/LivermoreN100iam/pic2d_N100.elf", 0x80001388L, 0x8000164cL),
    state_frag("org/specs/Riscv/asm/N100iam/state_frag_N100.elf", 0x80003940L, 0x80003b98L),
    tri_diag("org/specs/Riscv/asm/N100iam/tri_diag_N100.elf", 0x800030ecL, 0x80003248L);

    private String elfName;
    private Number kernelStart;
    private Number kernelStop;

    private RiscvLivermoreELFN100iam(String elfName, Number kernelStart, Number kernelStop) {
        this.elfName = elfName;
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
