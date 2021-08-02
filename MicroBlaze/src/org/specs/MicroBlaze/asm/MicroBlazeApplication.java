package org.specs.MicroBlaze.asm;

import org.specs.MicroBlaze.MicroBlazeELFProvider;
import org.specs.MicroBlaze.MicroBlazeResource;

import pt.up.fe.specs.binarytranslation.asm.Application;

public class MicroBlazeApplication extends Application {

    public MicroBlazeApplication(MicroBlazeELFProvider elfprovider) {
        super(elfprovider,
                MicroBlazeResource.MICROBLAZE_CPU_NAME,
                MicroBlazeResource.MICROBLAZE_GDB,
                MicroBlazeResource.MICROBLAZE_OBJDUMP,
                MicroBlazeResource.MICROBLAZE_READELF,
                MicroBlazeResource.QEMU_MICROBLAZE_GDB_TEMPLATE,
                MicroBlazeResource.QEMU_MICROBLAZE_GDBNONINTERACTIVE_TEMPLATE,
                MicroBlazeResource.QEMU_MICROBLAZE_EXE,
                MicroBlazeResource.QEMU_MICROBLAZE_BAREMETAL_DTB);
    }
}
