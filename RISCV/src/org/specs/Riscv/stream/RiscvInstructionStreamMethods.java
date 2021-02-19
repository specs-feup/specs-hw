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
