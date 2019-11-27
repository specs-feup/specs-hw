package org.specs.MicroBlaze;

import pt.up.fe.specs.util.providers.ResourceProvider;

public enum MicroBlazeResource implements ResourceProvider {

    QEMU_MICROBLAZE_GDB_TEMPLATE("org/specs/MicroBlaze/gdb/qemutmpl.gdb");

    private final String resource;

    private MicroBlazeResource(String resource) {
        this.resource = resource;
    }

    @Override
    public String getResource() {
        return resource;
    }
}
