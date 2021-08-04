package org.specs.Arm;

import pt.up.fe.specs.util.providers.ResourceProvider;

public enum ArmResource implements ResourceProvider {

    // FOR WINDOWS GNU TOOLS FOR ARM
    // https://developer.arm.com/tools-and-software/open-source-software/developer-tools/gnu-toolchain/gnu-a/downloads

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

    // https://www.xilinx.com/video/soc/running-bare-metal-application-using-qemu-command-line.html

    GDBINTERACTIVE_TEMPLATE("org/specs/Arm/gdb/qemutmpl.gdb"),
    GDBNONINTERACTIVE_TEMPLATE("org/specs/Arm/gdb/qemutmpl_noninteractive.gdb"),
    QEMU_AARCH64_BAREMETAL_DTB("org/specs/Arm/qemu/zcu102-arm.dtb"),
    QEMU_ARGS_TEMPLATE("org/specs/Arm/qemu/QEMUArgsArm");
    // <QEMUBIN> -nographic -M arm-generic-fdt -dtb <DTBFILE> -device loader,file=<ELFNAME>,cpu-num=0
    // -device loader,addr=0xfd1a0104,data=0x8000000e,data-len=4 -s -S

    private final String resource;

    private ArmResource(String resource) {
        this.resource = resource;
    }

    @Override
    public String getResource() {
        return resource;
    }
}
