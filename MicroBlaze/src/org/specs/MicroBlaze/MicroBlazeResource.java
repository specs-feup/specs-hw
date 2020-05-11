package org.specs.MicroBlaze;

import pt.up.fe.specs.util.providers.ResourceProvider;

public enum MicroBlazeResource implements ResourceProvider {

    MICROBLAZE_CPU_NAME("microblaze32"),
    QEMU_MICROBLAZE_GDB_TEMPLATE("org/specs/MicroBlaze/gdb/qemutmpl.gdb"),
    QEMU_MICROBLAZE_BAREMETAL_DTB("org/specs/MicroBlaze/qemu/system.dtb"),
    MICROBLAZE_GCC("mb-gcc"),
    MICROBLAZE_READELF("mb-readelf");

    private final String resource;

    private MicroBlazeResource(String resource) {
        this.resource = resource;
    }

    @Override
    public String getResource() {
        return resource;
    }
}
