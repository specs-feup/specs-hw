package org.specs.Arm;

import pt.up.fe.specs.util.providers.ResourceProvider;

public enum ArmResource implements ResourceProvider {

    ARMv8_CPU_NAME("aarch64"),

    QEMU_AARCH64_GDB_TEMPLATE("org/specs/Arm/gdb/qemutmpl.gdb"),
    QEMU_AARCH64_BAREMETAL_DTB("org/specs/Arm/qemu/zcu102-arm.dtb"),
    QEMU_AARCH64_EXE("qemu-system-aarch64"),
    // https://www.xilinx.com/video/soc/running-bare-metal-application-using-qemu-command-line.html

    AARCH64_GCC("aarch64-none-elf-gcc"),
    AARCH64_GDB("aarch64-none-elf-gdb"),
    AARCH64_READELF("aarch64-none-elf-readelf");

    private final String resource;

    private ArmResource(String resource) {
        this.resource = resource;
    }

    @Override
    public String getResource() {
        return resource;
    }
}
