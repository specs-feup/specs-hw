package org.specs.Arm;

import java.io.File;

import pt.up.fe.specs.binarytranslation.asm.Application;

public class ArmApplication extends Application {

    public ArmApplication(File elfName) {
        super(elfName,
                ArmResource.ARMv8_CPU_NAME,
                ArmResource.AARCH64_GDB,
                ArmResource.AARCH64_OBJDUMP,
                ArmResource.AARCH64_READELF,
                ArmResource.QEMU_AARCH64_GDB_TEMPLATE,
                ArmResource.QEMU_AARCH64_EXE,
                ArmResource.QEMU_AARCH64_BAREMETAL_DTB);
    }
}
