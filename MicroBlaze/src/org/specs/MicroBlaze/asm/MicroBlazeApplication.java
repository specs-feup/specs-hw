package org.specs.MicroBlaze.asm;

import org.specs.MicroBlaze.MicroBlazeResource;
import org.specs.MicroBlaze.provider.MicroBlazeELFProvider;

import pt.up.fe.specs.binarytranslation.asm.Application;

public class MicroBlazeApplication extends Application {

    public MicroBlazeApplication(MicroBlazeELFProvider elfprovider) {
        super(elfprovider);
        set(CPUNAME, "microblaze32");
        set(QEMUEXE, "qemu-system-microblazeel");
        set(GCC, "mb-gcc");
        set(GDB, "mb-gdb");
        set(READELF, "mb-readelf");
        set(OBJDUMP, "mb-objdump");
        set(GDBTMPLINTER, MicroBlazeResource.GDBINTERACTIVE_TEMPLATE);
        set(GDBTMPLNONINTER, MicroBlazeResource.GDBNONINTERACTIVE_TEMPLATE);
        set(BAREMETAL_DTB, MicroBlazeResource.BAREMETAL_DTB);
        set(QEMU_ARGS_TEMPLATE, MicroBlazeResource.QEMU_ARGS_TEMPLATE);
    }
}
