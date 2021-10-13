/**
 * Copyright 2021 SPeCS.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */
 
package org.specs.Riscv;

import pt.up.fe.specs.util.providers.ResourceProvider;

public enum RiscvResource implements ResourceProvider {

    // NEW: https://stackoverflow.com/questions/55189463/how-to-debug-cross-compiled-qemu-program-with-gdb

    GDBINTERACTIVE_TEMPLATE("org/specs/Riscv/gdb/qemutmpl.gdb"),
    GDBNONINTERACTIVE_TEMPLATE("org/specs/Riscv/gdb/qemutmpl_nointeractive.gdb"),
    QEMU_ARGS_TEMPLATE("org/specs/Riscv/qemu/QEMUArgsRiscv");
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

    // CRITICAL RESOURCE FOR GETTING RISC-V LD
    // https://twilco.github.io/riscv-from-scratch/2019/04/27/riscv-from-scratch-2.html#link-it-up

    // to get DTB from QEMU:
    // qemu-system-riscv32 -machine virt,dumpdtb=./virt.dtb
    // and if you want it in DTS
    // dtc -I dtb -O dts virt.dtb -o virt.dts

    // other resources: https://github.com/michaeljclark/riscv-probe/tree/master/libfemto

    private final String resource;

    private RiscvResource(String resource) {
        this.resource = resource;
    }

    @Override
    public String getResource() {
        return resource;
    }
}
