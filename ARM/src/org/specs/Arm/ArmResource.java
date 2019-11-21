package org.specs.Arm;

import pt.up.fe.specs.util.providers.ResourceProvider;

public enum ArmResource implements ResourceProvider {

    QEMU_AARCH64_GDB_TEMPLATE("org/specs/Arm/gdb/qemutmpl.gdb");

    private final String resource;

    private ArmResource(String resource) {
        this.resource = resource;
    }

    @Override
    public String getResource() {
        return resource;
    }

}
