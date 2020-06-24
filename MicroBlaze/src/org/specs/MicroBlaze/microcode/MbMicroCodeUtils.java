/**
 * Copyright 2020 SPeCS.
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

package org.specs.MicroBlaze.microcode;

import org.specs.MicroBlaze.legacy.MbRegister;

import pt.up.fe.specs.simulator.impl.Addr32;
import pt.up.fe.specs.simulator.microcodemachine.MicroCodeMachine;
import pt.up.fe.specs.simulator.microcodemachine.impl.Alu32;

public class MbMicroCodeUtils {

    /**
     * 
     * @return new MicroCodeMachine for 32-bit MicroBlaze
     */
    public static MicroCodeMachine newMicroBlaze32() {
        return newMicroBlaze32(false);
    }

    public static MicroCodeMachine newMicroBlaze32(boolean expandedDecoder) {
        var decoder = expandedDecoder ? new ExpandedMbMicroCodeDecoder() : new MbMicroCodeDecoder();

        return new MicroCodeMachine(number -> new Addr32(number.intValue()), MbRegister.RPC.ordinal(), new Alu32(),
                decoder);
    }

    public static Number signExtendedImm32(Number immValue, MicroCodeMachine machine) {
        return signExtendedImm(immValue, machine, 15);
    }

    /**
     * Sign-extend imm if no IMM value has been set, or fuse previous IMM value with given value.
     * 
     * <p>
     * If there is a value of IMM set, it will be unset after this instruction.
     * 
     * @param imm
     * @return
     */
    public static Number signExtendedImm(Number immValue, MicroCodeMachine machine, int signalBit) {
        // If there is a IMM value set, 'OR' it with given imm
        if (machine.getTemp().has(MbId.IMM)) {
            var upperImm = machine.getTemp().readAndClear(MbId.IMM);
            return machine.getAlu().or(upperImm, immValue);
        }

        // Sign-extend value
        return machine.getAlu().signExtend(immValue, signalBit);
    }

}
