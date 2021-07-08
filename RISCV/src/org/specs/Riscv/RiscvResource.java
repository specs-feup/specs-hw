package org.specs.Riscv;

import pt.up.fe.specs.util.providers.ResourceProvider;

public enum RiscvResource implements ResourceProvider {

    // NEW: https://stackoverflow.com/questions/55189463/how-to-debug-cross-compiled-qemu-program-with-gdb

    RISCV_CPU_NAME("riscv"),

    QEMU_RISCV_GDB_TEMPLATE("org/specs/Riscv/gdb/qemutmpl.gdb"),
    QEMU_RISCV_BAREMETAL_DTB(""),
    QEMU_RISCV_EXE("qemu-system-riscv32"),
    // SIVIFE: https://www.sifive.com/blog/risc-v-qemu-part-2-the-risc-v-qemu-port-is-upstream
    // The virt machine models a Generic RISC-V Virtual machine with support for the VirtIO standard networking and
    // block storage devices. It has CLINT, PLIC, 16550A UART devices in addition to VirtIO and it also uses device-tree
    // to pass configuration information to guest software.

    // VERY IMPORTANT: https://www.mdeditor.tw/pl/2FvU/zh-hk

    // https://github.com/riscv/riscv-gnu-toolchain/issues/571
    // to compile riscv for baremetal:
    // riscv32-unknown-elf gcc -ffreestanding -specs=nosys.specs -O2 -Wl,--no-relax -Wl,--gc-sections -nostartfiles
    // -Wl,-T,../riscv32-virt.ld $^ -o $@
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
    RISCV_OBJDUMP("riscv32-unknown-elf-objdump"),

    RISC_DUMP_REGEX("([0-9a-f]+):\\s([0-9a-f]+)"),
    RISC_TRACE_REGEX("0x([0-9a-f]+)\\s<.*>:\\s0x([0-9a-f]+)");

    private final String resource;

    private RiscvResource(String resource) {
        this.resource = resource;
    }

    @Override
    public String getResource() {
        return resource;
    }
}
