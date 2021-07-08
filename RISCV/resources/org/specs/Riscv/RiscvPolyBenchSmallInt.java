package org.specs.Riscv;

import org.specs.BinaryTranslation.ELFProvider;

public enum RiscvPolyBenchSmallInt implements ELFProvider {

    /*
     * output of riscv32-unknown-elf-objdump -t:
     * 
    8000025c 00000124 kernel_lu
    800000a0 0000013c kernel_jacobi_1d
    80000134 00000140 kernel_nussinov
    8000032c 00000240 kernel_ludcmp
    800001f4 000000a4 kernel_mvt
    80000194 000000cc kernel_trmm
    800003c0 00000178 kernel_gemver
    80000128 00000080 kernel_floyd_warshall
    80000240 00000144 kernel_symm
    8000013c 00000090 kernel_trisolv
    80000208 00000138 kernel_syr2k
    800001c8 000000d4 kernel_syrk
    80000228 000000e0 kernel_gemm
     */
    floydwarshall("org/specs/RISCV/asm/PolybenchSmallInt/floydwarshall.elf", 0x80000128L, 0x800001A4L),
    gemm("org/specs/RISCV/asm/PolybenchSmallInt/gemm.elf", 0x80000228L, 0x80000304L),
    gemver("org/specs/RISCV/asm/PolybenchSmallInt/gemver.elf", 0x800003c0L, 0x80000534L),
    jacobi1d("org/specs/RISCV/asm/PolybenchSmallInt/jacobi1d.elf", 0x800000a0L, 0x800001D8L),
    lu("org/specs/RISCV/asm/PolybenchSmallInt/lu.elf", 0x8000025cL, 0x8000037CL),
    ludcmp("org/specs/RISCV/asm/PolybenchSmallInt/ludcmp.elf", 0x8000032cL, 0x80000568L),
    mvt("org/specs/RISCV/asm/PolybenchSmallInt/mvt.elf", 0x800001f4L, 0x80000294L),
    nussinov("org/specs/RISCV/asm/PolybenchSmallInt/nussinov.elf", 0x80000134L, 0x80000270L),
    symm("org/specs/RISCV/asm/PolybenchSmallInt/symm.elf", 0x80000240L, 0x80000380L),
    syr2k("org/specs/RISCV/asm/PolybenchSmallInt/syr2k.elf", 0x80000208L, 0x8000033CL),
    syrk("org/specs/RISCV/asm/PolybenchSmallInt/syrk.elf", 0x800001c8L, 0x80000298L),
    trisolv("org/specs/RISCV/asm/PolybenchSmallInt/trisolv.elf", 0x8000013cL, 0x800001C8L),
    trmm("org/specs/RISCV/asm/PolybenchSmallInt/trmm.elf", 0x80000194L, 0x8000025CL);

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
