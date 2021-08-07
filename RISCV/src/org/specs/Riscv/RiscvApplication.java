package org.specs.Riscv;

import org.specs.Riscv.provider.RiscvELFProvider;

import pt.up.fe.specs.binarytranslation.asm.Application;

public class RiscvApplication extends Application {

    private static final String ARCHPREFIX = "riscv32-unknown-elf-";

    public RiscvApplication(RiscvELFProvider elfprovider) {
        super(elfprovider);
        set(CPUNAME, "riscv");
        set(QEMUEXE, "qemu-system-riscv32");
        set(GCC, ARCHPREFIX + "gcc");
        set(GDB, ARCHPREFIX + "gdb");
        set(READELF, ARCHPREFIX + "readelf");
        set(OBJDUMP, ARCHPREFIX + "objdump");
        set(GDBTMPLINTER, RiscvResource.GDBINTERACTIVE_TEMPLATE);
        set(GDBTMPLNONINTER, RiscvResource.GDBNONINTERACTIVE_TEMPLATE);
        set(QEMU_ARGS_TEMPLATE, RiscvResource.QEMU_ARGS_TEMPLATE);

        /*
         * 
        RISCV_GCC("riscv32-unknown-elf-gcc"),
        RISCV_GDB("riscv32-unknown-elf-gdb"),
        RISCV_READELF("riscv32-unknown-elf-readelf"),
        RISCV_OBJDUMP("riscv32-unknown-elf-objdump");
        
         */

        /*
        super(elfprovider,
                RiscvResource.RISCV_CPU_NAME,
                RiscvResource.RISCV_GDB,
                RiscvResource.RISCV_OBJDUMP,
                RiscvResource.RISCV_READELF,
                RiscvResource.QEMU_RISCV_GDB_TEMPLATE,
                RiscvResource.QEMU_RISCV_GDBNONINTERACTIVE_TEMPLATE,
                RiscvResource.QEMU_RISCV_EXE,
                null);*/
    }
}
