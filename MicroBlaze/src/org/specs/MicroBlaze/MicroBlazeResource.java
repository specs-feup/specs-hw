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

package org.specs.MicroBlaze;

import pt.up.fe.specs.util.providers.ResourceProvider;

public enum MicroBlazeResource implements ResourceProvider {

    // FOR WINDOWS GNU TOOLS FOR MICROBLAZE
    // https://www.xilinx.com/products/design-tools/guest-resources.html#2020

    // FOR QEMU BUILDS FOR MICROBLAZE, SEE THE XILINX QEMU FORK
    // https://github.com/Xilinx/qemu.git

    // NOTE: if libcurl3 is missing in ubuntu 20.04, do this:
    // 1. add "deb http://cz.archive.ubuntu.com/ubuntu bionic main universe" to /etc/apt/sources.list
    // 2. sudo apt-get update
    // 3. sudo apt-get install libcurl3
    // 4. remove the entry from /etc/apt/sources.list
    // 5. sudo apt-get update

    GDBINTERACTIVE_TEMPLATE("org/specs/MicroBlaze/gdb/qemutmpl.gdb"),
    GDBNONINTERACTIVE_TEMPLATE("org/specs/MicroBlaze/gdb/qemutmpl_noninteractive.gdb"),
    BAREMETAL_DTB("org/specs/MicroBlaze/qemu/2021.1/system.dtb"),
    BAREMETAL_DTB_4GB("org/specs/MicroBlaze/qemu/2021.1_4GB_manual/system.dtb"),
    QEMU_ARGS_TEMPLATE("org/specs/MicroBlaze/qemu/QEMUArgsMicroBlaze");

    private final String resource;

    private MicroBlazeResource(String resource) {
        this.resource = resource;
    }

    @Override
    public String getResource() {
        return resource;
    }
}
