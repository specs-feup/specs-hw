package org.specs.Riscv;

import java.io.File;

import pt.up.fe.specs.binarytranslation.asm.Application;

public class RiscvApplication extends Application {

    public RiscvApplication(File elfName) {
        super(elfName,
                RiscvResource.RISCV_CPU_NAME,
                RiscvResource.RISCV_GDB,
                RiscvResource.RISCV_OBJDUMP,
                RiscvResource.RISCV_READELF,
                RiscvResource.QEMU_RISCV_GDB_TEMPLATE,
                RiscvResource.QEMU_RISCV_EXE,
                RiscvResource.QEMU_RISCV_BAREMETAL_DTB);
    }
}
