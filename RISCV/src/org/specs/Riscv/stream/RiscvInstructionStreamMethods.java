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
 
package org.specs.Riscv.stream;

import java.util.regex.Pattern;

import org.specs.Riscv.instruction.RiscvInstruction;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.util.SpecsStrings;
import pt.up.fe.specs.util.utilities.LineStream;

public class RiscvInstructionStreamMethods {

    public static Instruction nextInstruction(LineStream insts, Pattern regex) {
        System.out.println(insts.getReadLines());
        String line = null;
        
        while (((line = insts.nextLine()) != null) && !SpecsStrings.matches(line, regex)) {
            
        }

        if (line == null) {
            return null;
        }

        var addressAndInst = SpecsStrings.getRegex(line, regex);
        var addr = addressAndInst.get(0).trim();
        var inst = addressAndInst.get(1).trim();
        var newinst = RiscvInstruction.newInstance(addr, inst);
        return newinst;
    }

    public static int getInstructionWidth() {
        return 4; // return in bytes
        // TODO replace this with something smarter
    }
}
