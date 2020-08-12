package org.specs.Arm;

import pt.up.fe.specs.util.providers.ResourceProvider;

public enum ArmResource implements ResourceProvider {

    // README:
    // One thing to remember is that the libusb libs that come with Vivado are probably meant to be used with
    // the QEMU versions found in https://github.com/Xilinx/qemu
    // That is, if you checkout a new QEMU build, you may need to install the lastest vivado, or at least
    // force QEMU to rely on system libs!
    // Regardless, in order to make the trace stream for aarch64 work currently:
    // 1. Source Vivado settings script
    // 2. run "unset LD_LIBRARY_PATH"
    // 3. Use the "qemutmpl.dtb" template, which relies on the arm-generic-fdt ONLY SUPPORTED BY THE XILINX BUILD OF
    // QEMU!

    ARMv8_CPU_NAME("aarch64"),

    QEMU_AARCH64_GDB_TEMPLATE("org/specs/Arm/gdb/qemutmpl.gdb"),
    QEMU_AARCH64_BAREMETAL_DTB("org/specs/Arm/qemu/zcu102-arm.dtb"),
    QEMU_AARCH64_EXE("/media/nuno/HDD/work/projects/xilinx-repos/qemu/aarch64-softmmu/qemu-system-aarch64"),
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
