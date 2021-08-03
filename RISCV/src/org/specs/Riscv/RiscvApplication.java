package org.specs.Riscv;

import pt.up.fe.specs.binarytranslation.asm.Application;

public class RiscvApplication extends Application {

    public RiscvApplication(RiscvELFProvider elfprovider) {
        super(elfprovider,
                RiscvResource.RISCV_CPU_NAME,
                RiscvResource.RISCV_GDB,
                RiscvResource.RISCV_OBJDUMP,
                RiscvResource.RISCV_READELF,
                RiscvResource.QEMU_RISCV_GDB_TEMPLATE,
                RiscvResource.QEMU_RISCV_GDBNONINTERACTIVE_TEMPLATE,
                RiscvResource.QEMU_RISCV_EXE,
                null);
    }
}
