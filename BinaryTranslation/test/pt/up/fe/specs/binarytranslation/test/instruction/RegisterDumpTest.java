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
 
package pt.up.fe.specs.binarytranslation.test.instruction;

import pt.up.fe.specs.binarytranslation.elf.ELFProvider;
import pt.up.fe.specs.binarytranslation.instruction.register.RegisterDump;
import pt.up.fe.specs.binarytranslation.processes.GDBRun;

public class RegisterDumpTest {

    protected static void TestRegisterDump(RegisterDump dump) {

        var map = dump.getRegisterMap();
        for (var regdef : map.keySet())
            System.out.println(regdef.getName() + " = " + map.get(regdef));

        // dump.getValue(MicroBlazeRegister.R1);
    }

    protected static void GetGDBResponse(ELFProvider elf) {
        var app = elf.toApplication();
        try (var gdb = GDBRun.newInstanceInteractive(app)) {

            // launch QEMU, then GDB (on localhost:1234)
            gdb.start();

            // run until kernel start
            gdb.runUntil(app.getELFProvider().getFunctionName());

            System.out.println(gdb.getRegisters());
        }
    }
}
