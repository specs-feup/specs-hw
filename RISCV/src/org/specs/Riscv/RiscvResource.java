package org.specs.Riscv;

import pt.up.fe.specs.util.providers.ResourceProvider;

public enum RiscvResource implements ResourceProvider {

    // https://github.com/riscv/riscv-qemu/wiki
    RISCV_CPU_NAME("riscv"),
    QEMU_RISCV_GDB_TEMPLATE(""),
    QEMU_RISCV_BAREMETAL_DTB(""),
    RISCV_GCC(""),
    RISCV_READELF("");

    private final String resource;

    private RiscvResource(String resource) {
        this.resource = resource;
    }

    @Override
    public String getResource() {
        return resource;
    }
}
