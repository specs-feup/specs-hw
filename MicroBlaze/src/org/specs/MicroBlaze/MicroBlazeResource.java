package org.specs.MicroBlaze;

import pt.up.fe.specs.util.providers.ResourceProvider;

public enum MicroBlazeResource implements ResourceProvider {

    // NOTE: if libcurl3 is missing in ubuntu 20.04, do this:
    // 1. add "deb http://cz.archive.ubuntu.com/ubuntu bionic main universe" to /etc/apt/sources.list
    // 2. sudo apt-get update
    // 3. sudo apt-get install libcurl3
    // 4. remove the entry from /etc/apt/sources.list
    // 5. sudo apt-get update

    MICROBLAZE_CPU_NAME("microblaze32"),

//    QEMU_MICROBLAZE_GDB_TEMPLATE("org/specs/MicroBlaze/gdb/qemutmpl.gdb"),
//    QEMU_MICROBLAZE_BAREMETAL_DTB("org/specs/MicroBlaze/qemu/system.dtb"),
//    QEMU_MICROBLAZE_EXE("/media/nuno/HDD/work/projects/myqemus/qemu-system-microblazeel"), // TODO fix
//
//    MICROBLAZE_GCC("mb-gcc"),
//    MICROBLAZE_GDB("mb-gdb"),
//    MICROBLAZE_READELF("mb-readelf"),
//    MICROBLAZE_OBJDUMP("mb-objdump"),
    
    QEMU_MICROBLAZE_GDB_TEMPLATE("org/specs/MicroBlaze/gdb/qemutmpl-registers.gdb"),
    QEMU_MICROBLAZE_BAREMETAL_DTB("org/specs/MicroBlaze/qemu/system.dtb"),
    QEMU_MICROBLAZE_EXE("/home/ubuntu/qemu/qemu-system-microblazeel"), 

    MICROBLAZE_GCC("/home/ubuntu/Xilinx/Vivado/2020.2/gnu/microblaze/lin/bin/mb-gcc"),
    MICROBLAZE_GDB("/home/ubuntu/Xilinx/Vivado/2020.2/gnu/microblaze/lin/bin/mb-gdb"),
    MICROBLAZE_READELF("/home/ubuntu/Xilinx/Vivado/2020.2/gnu/microblaze/lin/bin/mb-readelf"),
    MICROBLAZE_OBJDUMP("/home/ubuntu/Xilinx/Vivado/2020.2/gnu/microblaze/lin/bin/mb-objdump"),

    MICROBLAZE_INSTRUCTION_DUMP_REGEX("([0-9a-f]+):\\s([0-9a-f]+)"),
    MICROBLAZE_INSTRUCTION_TRACE_REGEX("0x([0-9a-f]+)\\s<.*>:\\s0x([0-9a-f]+)");

    private final String resource;

    private MicroBlazeResource(String resource) {
        this.resource = resource;
    }

    @Override
    public String getResource() {
        return resource;
    }
}
