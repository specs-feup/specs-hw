package org.specs.Riscv;

import pt.up.fe.specs.binarytranslation.ELFProvider;

public enum RiscvLivermoreELFN100iamf implements ELFProvider {

    cholesky100("org/specs/Riscv/asm/N100iamf/cholesky_N100iamf.elf", 0x80003060L, 0x80003148L),
    diffpredict100("org/specs/Riscv/asm/N100iamf/diffpredict_N100iamf.elf", 0x8000119cL, 0x80001260L),
    // glinearrec100("org/specs/Riscv/asm/N100iamf/glinearrec_N100iamf.elf", 0x80001ad4L, 0x80001c6cL), //
    // linrecurrence?
    // (there
    // are
    // 2
    // linear
    // recurrence kernels)
    hydro100("org/specs/Riscv/asm/N100iamf/hydro_N100iamf.elf", 0x80002e48L, 0x80002f24L),
    hydro2d100("org/specs/Riscv/asm/N100iamf/hydro2d_N100iamf.elf", 0x80003510L, 0x80003990L),
    hydro2dimpl100("org/specs/Riscv/asm/N100iamf/hydro2dimpl_N100iamf.elf", 0x80003268L, 0x8000350cL),
    innerprod100("org/specs/Riscv/asm/N100iamf/innerprod_N100iamf.elf", 0x80001130L, 0x80001198L),
    intpredict100("org/specs/Riscv/asm/N100iamf/intpredict_N100iamf.elf", 0x80001df8L, 0x80001f6cL),
    linrec100("org/specs/Riscv/asm/N100iamf/linrec_N100iamf.elf", 0x80002d38L, 0x80002e44L),
    matmul100("org/specs/Riscv/asm/N100iamf/matmul_N100iamf.elf", 0x80002f28L, 0x8000305cL),
    pic1d100("org/specs/Riscv/asm/N100iamf/pic1d_N100iamf.elf", 0x80001450L, 0x8000179cL),
    pic2d100("org/specs/Riscv/asm/N100iamf/pic2d_N100iamf.elf", 0x80001264L, 0x8000144cL),
    state_frag100("org/specs/Riscv/asm/N100iamf/state_frag_N100iamf.elf", 0x8000314cL, 0x80003264L),
    tri_diag100("org/specs/Riscv/asm/N100iamf/tri_diag_N100iamf.elf", 0x80002c6cL, 0x80002d34L);

    private String fullPath;
    private Number kernelStart;
    private Number kernelStop;

    private RiscvLivermoreELFN100iamf(String fullPath, Number kernelStart, Number kernelStop) {
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
