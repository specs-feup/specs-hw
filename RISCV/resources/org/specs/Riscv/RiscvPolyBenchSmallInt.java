package org.specs.Riscv;

import org.specs.BinaryTranslation.ELFProvider;

public enum RiscvPolyBenchSmallInt implements ELFProvider {

    floyd("org/specs/Riscv/asm/PolyBenchSmallInteger/floyd-warshall.elf", 0x80000120L, 0x8000019cL),
    gemm("org/specs/Riscv/asm/PolyBenchSmallInteger/gemm.elf", 0x8000011cL, 0x800001f8L),
    gemver("org/specs/Riscv/asm/PolyBenchSmallInteger/gemver.elf", 0x80000344L, 0x800004b8L),
    jacobi1d("org/specs/Riscv/asm/PolyBenchSmallInteger/jacobi-1d.elf", 0x80000094L, 0x800001ccL),
    lu("org/specs/Riscv/asm/PolyBenchSmallInteger/lu.elf", 0x8000023cL, 0x8000035cL),
    ludcmp("org/specs/Riscv/asm/PolyBenchSmallInteger/ludcmp.elf", 0x80000234L, 0x80000560L),
    mvt("org/specs/Riscv/asm/PolyBenchSmallInteger/mvt.elf", 0x80000160L, 0x80000200L),
    nussinov("org/specs/Riscv/asm/PolyBenchSmallInteger/nussinov.elf", 0x800000d8L, 0x80000214L),
    symm("org/specs/Riscv/asm/PolyBenchSmallInteger/symm.elf", 0x800001c0L, 0x80000300L),
    syrk2k("org/specs/Riscv/asm/PolyBenchSmallInteger/syrk2k.elf", 0x80000160L, 0x80000294L),
    syrk("org/specs/Riscv/asm/PolyBenchSmallInteger/syrk.elf", 0x800000e4L, 0x800001b4L),
    trisolv("org/specs/Riscv/asm/PolyBenchSmallInteger/trisolv.elf", 0x80000114L, 0x800001a0L),
    trmm("org/specs/Riscv/asm/PolyBenchSmallInteger/trmm.elf", 0x80000148L, 0x80000210L);

    private String fullPath;
    private Number kernelStart;
    private Number kernelStop;

    private RiscvPolyBenchSmallInt(String fullPath, Number kernelStart, Number kernelStop) {
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
