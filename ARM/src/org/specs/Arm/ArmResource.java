package org.specs.Arm;

import pt.up.fe.specs.util.providers.ResourceProvider;

public enum ArmResource implements ResourceProvider {

    // README:
    // How to QEMU for AARCH64 after running "sudo apt-get upgrade qemu*"
    // 1. Source the settings64.sh script for Vivado
    // 2. do "unset LD_LIBRARY_PATH" so that the /usr/bin/qemu-system-aarch64 executable doesn't try to use the
    // libusb libs that come packaged with Vivado (they are older libs without libusbredirparser)
    // 3. use the "qemutmpl_nodtb.gbd" script, since the new version of qemu-system-aarch64 apparently has
    // issues with the "arm-generic-fdt" machine target after upgrade combined with sourcing of vivado script...

    ARMv8_CPU_NAME("aarch64"),

    QEMU_AARCH64_GDB_TEMPLATE("org/specs/Arm/gdb/qemutmpl_nodtb.gdb"),
    QEMU_AARCH64_BAREMETAL_DTB("org/specs/Arm/qemu/zcu102-arm.dtb"),
    QEMU_AARCH64_EXE("qemu-system-aarch64"),
    // https://www.xilinx.com/video/soc/running-bare-metal-application-using-qemu-command-line.html

    AARCH64_GCC("aarch64-none-elf-gcc"),
    AARCH64_GDB("aarch64-none-elf-gdb"),
    AARCH64_READELF("aarch64-none-elf-readelf"),
    AARCH64_OBJDUMP("aarch64-none-elf-objdump");

    private final String resource;

    private ArmResource(String resource) {
        this.resource = resource;
    }

    @Override
    public String getResource() {
        return resource;
    }
}
