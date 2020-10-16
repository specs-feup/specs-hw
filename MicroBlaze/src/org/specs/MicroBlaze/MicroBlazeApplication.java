package org.specs.MicroBlaze;

import java.io.File;

import pt.up.fe.specs.binarytranslation.asm.Application;

public class MicroBlazeApplication extends Application {

    public MicroBlazeApplication(File elfName) {
        super(elfName,
                MicroBlazeResource.MICROBLAZE_CPU_NAME,
                MicroBlazeResource.MICROBLAZE_GDB,
                MicroBlazeResource.MICROBLAZE_OBJDUMP,
                MicroBlazeResource.MICROBLAZE_READELF,
                MicroBlazeResource.QEMU_MICROBLAZE_GDB_TEMPLATE,
                MicroBlazeResource.QEMU_MICROBLAZE_EXE,
                MicroBlazeResource.QEMU_MICROBLAZE_BAREMETAL_DTB);
    }
}
