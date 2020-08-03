package org.specs.Riscv;

import pt.up.fe.specs.util.providers.ResourceProvider;

public enum RiscvResource implements ResourceProvider {

    RISCV_CPU_NAME("riscv"),

    QEMU_RISCV_GDB_TEMPLATE(""),
    QEMU_RISCV_BAREMETAL_DTB(""),
    QEMU_RISCV_EXE(""),
    // https://github.com/riscv/riscv-qemu/wiki
    // https://github.com/michaeljclark/riscv-probe
    // https://theintobooks.wordpress.com/2019/12/28/hello-world-on-risc-v-with-qemu/
    // https://github.com/noteed/riscv-hello-c
    // https://matrix89.github.io/writes/writes/experiments-in-riscv/
    // https://twilco.github.io/riscv-from-scratch/2019/04/27/riscv-from-scratch-2.html#link-it-up

    // other resources: https://github.com/michaeljclark/riscv-probe/tree/master/libfemto

    RISCV_GCC("riscv32-unknown-elf-gcc"),
    RISCV_GDB("riscv32-unknown-elf-gdb"),
    RISCV_READELF("riscv32-unknown-elf-readelf"),
    RISCV_OBJDUMP("riscv32-unknown-elf-objdump");

    private final String resource;

    private RiscvResource(String resource) {
        this.resource = resource;
    }

    @Override
    public String getResource() {
        return resource;
    }
}
