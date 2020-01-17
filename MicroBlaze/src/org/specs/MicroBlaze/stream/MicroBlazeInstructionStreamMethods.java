package org.specs.MicroBlaze.stream;

import java.util.regex.Pattern;

import org.specs.MicroBlaze.instruction.MicroBlazeInstruction;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.util.SpecsStrings;
import pt.up.fe.specs.util.utilities.LineStream;

public class MicroBlazeInstructionStreamMethods {

    public static Instruction nextInstruction(LineStream insts, Pattern regex) {

        String line = null;
        while (((line = insts.nextLine()) != null) && !SpecsStrings.matches(line, regex))
            ;

        if (line == null) {
            return null;
        }

        var addressAndInst = SpecsStrings.getRegex(line, regex);
        var addr = addressAndInst.get(0).trim();
        var inst = addressAndInst.get(1).trim();
        var newinst = MicroBlazeInstruction.newInstance(addr, inst);
        return newinst;
    }

    public static int getInstructionWidth() {
        return 4; // return in bytes
        // TODO replace this with something smarter
    }

    /*
    @Override
    public Instruction nextInstruction() {
    
        Instruction i = null;
    
        if (haveStoredInst == true) {
            i = afterbug;
            haveStoredInst = false;
        }
    
        else {
            String line = null;
            while (((line = insts.nextLine()) != null) && !SpecsStrings.matches(line, REGEX))
                ;
    
            if (line == null) {
                return null;
            }
    
            var addressAndInst = SpecsStrings.getRegex(line, REGEX);
            var addr = addressAndInst.get(0).trim();
            var inst = addressAndInst.get(1).trim();
            i = MicroBlazeInstruction.newInstance(addr, inst);
        }
    
        // get another one if true
         if (i.isImmediateValue() || (i.getDelay() > 0)) {
            afterbug = elfdump.get(i.getAddress().intValue() + this.getInstructionWidth());
            haveStoredInst = true;
        }
    
        this.numcycles += i.getLatency();
        this.numinsts++;
        return i;
    }
    */
}
