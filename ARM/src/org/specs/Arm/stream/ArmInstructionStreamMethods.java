package org.specs.Arm.stream;

import java.util.regex.Pattern;

import org.specs.Arm.instruction.ArmInstruction;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.util.SpecsStrings;
import pt.up.fe.specs.util.utilities.LineStream;

public class ArmInstructionStreamMethods {

    /*
     * Used by both static and trace streams for ARM 
     */
    protected static Instruction nextInstruction(LineStream insts, Pattern regex) {

        String line = null;
        while (((line = insts.nextLine()) != null) && !SpecsStrings.matches(line, regex))
            ;

        if (line == null) {
            return null;
        }

        var addressAndInst = SpecsStrings.getRegex(line, regex);
        var addr = addressAndInst.get(0).trim();
        var inst = addressAndInst.get(1).trim();
        var newinst = ArmInstruction.newInstance(addr, inst);
        return newinst;
    }

    /*
     * Used by both static and trace streams for ARM 
     */
    public static int getInstructionWidth() {
        return 4; // return in bytes
        // TODO replace this with something smarter
    }
}
