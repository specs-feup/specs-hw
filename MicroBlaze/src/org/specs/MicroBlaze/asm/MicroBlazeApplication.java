package org.specs.MicroBlaze.asm;

import org.specs.MicroBlaze.MicroBlazeResource;
import org.specs.MicroBlaze.provider.MicroBlazeELFProvider;
import org.suikasoft.jOptions.DataStore.SimpleDataStore;
import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.binarytranslation.asm.Application;

public class MicroBlazeApplication extends Application {

    private static final String ARCHPREFIX = "mb-";
    private static final DataStore data;
    static {
        data = new SimpleDataStore("MicroBlazeApplicationDataStore");
        data.set(CPUNAME, "microblaze32");
        data.set(QEMUEXE, "qemu-system-microblazeel");
        data.set(GCC, ARCHPREFIX + "gcc");
        data.set(GDB, ARCHPREFIX + "gdb");
        data.set(READELF, ARCHPREFIX + "readelf");
        data.set(OBJDUMP, ARCHPREFIX + "objdump");
        data.set(GDBTMPLINTER, MicroBlazeResource.GDBINTERACTIVE_TEMPLATE);
        data.set(GDBTMPLNONINTER, MicroBlazeResource.GDBNONINTERACTIVE_TEMPLATE);
        data.set(BAREMETAL_DTB, MicroBlazeResource.BAREMETAL_DTB);
        data.set(QEMU_ARGS_TEMPLATE, MicroBlazeResource.QEMU_ARGS_TEMPLATE);
    }

    public MicroBlazeApplication(MicroBlazeELFProvider elfprovider) {
        super(elfprovider, data);
    }
}
