package org.specs.Arm;

import org.specs.Arm.provider.ArmELFProvider;
import org.suikasoft.jOptions.DataStore.SimpleDataStore;
import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.binarytranslation.asm.Application;

public class ArmApplication extends Application {

    private static final String ARCHPREFIX = "aarch64-";
    private static final DataStore data;
    static {
        data = new SimpleDataStore("ArmApplicationDataStore");
        data.set(CPUNAME, "aarch64");
        data.set(QEMUEXE, "qemu-system-aarch64");
        data.set(GCC, ARCHPREFIX + "gcc");
        data.set(GDB, ARCHPREFIX + "gdb");
        data.set(READELF, ARCHPREFIX + "readelf");
        data.set(OBJDUMP, ARCHPREFIX + "objdump");
        data.set(GDBTMPLINTER, ArmResource.GDBINTERACTIVE_TEMPLATE);
        data.set(GDBTMPLNONINTER, ArmResource.GDBNONINTERACTIVE_TEMPLATE);
        data.set(BAREMETAL_DTB, ArmResource.BAREMETAL_DTB);
        data.set(QEMU_ARGS_TEMPLATE, ArmResource.QEMU_ARGS_TEMPLATE);
    }

    public ArmApplication(ArmELFProvider elfprovider) {
        super(elfprovider, data);
    }
}
