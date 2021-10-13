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
 
package org.specs.Arm;

import org.specs.Arm.provider.ArmELFProvider;
import org.suikasoft.jOptions.DataStore.SimpleDataStore;
import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.binarytranslation.asm.Application;

public class ArmApplication extends Application {

    private static final String ARCHPREFIX = "aarch64-xilinx-elf-";
    private static final DataStore data;
    static {
        data = new SimpleDataStore("ArmApplicationDataStore");
        data.set(CPUNAME, "aarch64");
        data.set(QEMUEXE, "qemu-system-aarch64");
        data.set(GCC, ARCHPREFIX + "gcc");
        data.set(GDB, ARCHPREFIX + "gdb");
        data.set(READELF, ARCHPREFIX + "readelf");
        data.set(OBJDUMP, ARCHPREFIX + "objdump");
        data.set(GDBTMPLINTER, ArmResource.GDBINTERACTIVE_TEMPLATE);
        data.set(GDBTMPLNONINTER, ArmResource.GDBNONINTERACTIVE_TEMPLATE);
        data.set(BAREMETAL_DTB, ArmResource.BAREMETAL_DTB);
        data.set(QEMU_ARGS_TEMPLATE, ArmResource.QEMU_ARGS_TEMPLATE);
    }

    public ArmApplication(ArmELFProvider elfprovider) {
        super(elfprovider, data);
    }
}
