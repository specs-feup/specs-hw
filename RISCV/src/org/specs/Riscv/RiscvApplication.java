package org.specs.Riscv;

import org.specs.Riscv.provider.RiscvELFProvider;
import org.suikasoft.jOptions.DataStore.SimpleDataStore;
import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.binarytranslation.NullResource;
import pt.up.fe.specs.binarytranslation.asm.Application;

public class RiscvApplication extends Application {

    private static final String ARCHPREFIX = "riscv32-unknown-elf-";
    private static final DataStore data;
    static {
        data = new SimpleDataStore("RiscVApplicationDataStore");
        data.set(CPUNAME, "riscv");
        data.set(QEMUEXE, "qemu-system-riscv32");
        data.set(GCC, ARCHPREFIX + "gcc");
        data.set(GDB, ARCHPREFIX + "gdb");
        data.set(READELF, ARCHPREFIX + "readelf");
        data.set(OBJDUMP, ARCHPREFIX + "objdump");
        data.set(GDBTMPLINTER, RiscvResource.GDBINTERACTIVE_TEMPLATE);
        data.set(GDBTMPLNONINTER, RiscvResource.GDBNONINTERACTIVE_TEMPLATE);
        data.set(BAREMETAL_DTB, NullResource.nullResource);
        data.set(QEMU_ARGS_TEMPLATE, RiscvResource.QEMU_ARGS_TEMPLATE);
    }

    public RiscvApplication(RiscvELFProvider elfprovider) {
        super(elfprovider, data);
    }
}
