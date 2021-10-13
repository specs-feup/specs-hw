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
 
package org.specs.MicroBlaze.test.instruction;

import org.junit.Test;
import org.specs.MicroBlaze.instruction.MicroBlazeRegisterDump;
import org.specs.MicroBlaze.provider.MicroBlazeLivermoreN100;

import pt.up.fe.specs.binarytranslation.test.instruction.RegisterDumpTest;

public class MicroBlazeRegisterDumpTest extends RegisterDumpTest {

    private static String gdbResponse = "r0             0x0                 0\n"
            + "r1             0x1f0f0             127216\n"
            + "r2             0x6440              25664\n"
            + "r3             0x2614              9748\n"
            + "r4             0xbf8147ae          -1082046546\n"
            + "r5             0x1f144             127300\n"
            + "r6             0x73c0              29632\n"
            + "r7             0x101               257\n"
            + "r8             0x400               1024\n"
            + "r9             0x681c              26652\n"
            + "r10            0xf                 15\n"
            + "r11            0x6544              25924\n"
            + "r12            0x67f4              26612\n"
            + "r13            0x6b20              27424\n"
            + "r14            0x0                 0\n"
            + "r15            0x59d4              22996\n"
            + "r16            0x0                 0\n"
            + "r17            0x0                 0\n"
            + "r18            0x0                 0\n"
            + "r19            0x1f144             127300\n"
            + "r20            0x0                 0\n"
            + "r21            0x0                 0\n"
            + "r22            0x2                 2\n"
            + "r23            0x8                 8\n"
            + "r24            0x1                 1\n"
            + "r25            0x69bc              27068\n"
            + "r26            0x6980              27008\n"
            + "r27            0x69f8              27128\n"
            + "r28            0x0                 0\n"
            + "r29            0x0                 0\n"
            + "r30            0x0                 0\n"
            + "r31            0x0                 0\n"
            + "rpc            0x2614              9748\n"
            + "rmsr           0x0                 0\n"
            + "rear           0x0                 0\n"
            + "resr           0x0                 0\n"
            + "rfsr           0x0                 0\n"
            + "rbtr           0x0                 0\n"
            + "rpvr0          0x7f202500          2132813056\n"
            + "rpvr1          0x0                 0\n"
            + "rpvr2          0xf0017a01          -268338687\n"
            + "rpvr3          0x0                 0\n"
            + "rpvr4          0x0                 0\n"
            + "rpvr5          0x0                 0\n"
            + "rpvr6          0x0                 0\n"
            + "rpvr7          0x0                 0\n"
            + "rpvr8          0x0                 0\n"
            + "rpvr9          0x0                 0\n"
            + "rpvr10         0xc000000           201326592\n"
            + "rpvr11         0x200000            2097152\n"
            + "redr           0x0                 0\n"
            + "rpid           0x0                 0\n"
            + "rzpr           0x0                 0\n"
            + "rtlbx          0x0                 0\n"
            + "rtlbsx         0x0                 0\n"
            + "rtlblo         0x0                 0\n"
            + "rtlbhi         0x0                 0\n"
            + "rslr           0xffffffff00000000  18446744069414584320\n"
            + "rshr           0x55b9ffffffff      94257352278015";

    /**
     * Construction of a @MicroBlazeRegisterDump from the raw gdbresponse string. This process is hidden within the
     * "nextInstruction()" flow of the @TraceInstructionStream (if this instance contains a @TraceInstructionProducer
     * which is a @GDBRun). The @ARegisterDump class initializes a map where all values of the registers for the given
     * ISA can be queried using the list of register definitions e.g., @MicroBlazeRegister
     */
    @Test
    public void MicroBlazeTestDump() {
        TestRegisterDump(new MicroBlazeRegisterDump(gdbResponse));
    }

    @Test
    public void auxGetGDBResponse() {
        GetGDBResponse(MicroBlazeLivermoreN100.innerprod);
    }
}
