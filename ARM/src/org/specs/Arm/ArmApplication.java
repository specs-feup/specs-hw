package org.specs.Arm;

import org.specs.Arm.provider.ArmELFProvider;

import pt.up.fe.specs.binarytranslation.asm.Application;

public class ArmApplication extends Application {

    public ArmApplication(ArmELFProvider elfprovider) {
        super(elfprovider);
        set(CPUNAME, "aarch64");
        set(QEMUEXE, "qemu-system-aarch64");
        set(GCC, "aarch64-none-elf-gcc");
        set(GDB, "aarch64-none-elf-gdb");
        set(READELF, "aarch64-none-elf-readelf");
        set(OBJDUMP, "aarch64-none-elf-objdump");
        set(GDBTMPLINTER, ArmResource.GDBINTERACTIVE_TEMPLATE);
        set(GDBTMPLNONINTER, ArmResource.GDBNONINTERACTIVE_TEMPLATE);
        set(BAREMETAL_DTB, ArmResource.QEMU_AARCH64_BAREMETAL_DTB);
        set(QEMU_ARGS_TEMPLATE, ArmResource.QEMU_ARGS_TEMPLATE);
    }
}
