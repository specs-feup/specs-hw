package org.specs.MicroBlaze.stream;

import java.io.File;
import java.util.regex.Pattern;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.AStaticInstructionStream;

public class MicroBlazeElfStream extends AStaticInstructionStream {

    private static final String OBJDUMP_EXE = "mb-objdump";
    private static final Pattern REGEX = Pattern.compile("([0-9a-f]+):\\s([0-9a-f]+)");

    public MicroBlazeElfStream(File elfname) {
        super(elfname, OBJDUMP_EXE);
    }

    @Override
    public Instruction nextInstruction() {

        var newinst = MicroBlazeInstructionStreamMethods.nextInstruction(this.insts, REGEX);
        if (newinst == null) {
            return null;
        }
        this.numcycles += newinst.getLatency();
        this.numinsts++;
        return newinst;
    }

    @Override
    public int getInstructionWidth() {
        return MicroBlazeInstructionStreamMethods.getInstructionWidth();
    }

    // THIS METHOD WORKS, BUT I NEED A PURE STREAM FOR THE CURRENT IMPLEMENTATION
    // OF THE FREQUENT SEQUENCE DETECTOR
    /*
     * Absorb an imm into the next 
     * instruction, if it is an imm
     */
    /*
    @Override
    public Instruction nextInstruction() {
    
        Instruction i = getInstruction();
        if (i == null)
            return i;
    
        if (i.isImmediateValue()) {
    
            // get imm value
            var ops1 = i.getData().getOperands();
            int immval = ops1.get(0).getValue().intValue();
    
            // new instruction, and last operand (should be the imm value)
            var i2 = getInstruction();
            var ops2 = i2.getData().getOperands();
            var op = ops2.get(ops2.size() - 1);
    
            MicroBlazeAsmField field = (MicroBlazeAsmField) op.getAsmField();
            Operand replacer = MicroBlazeOperand.newImmediate(field, (immval << 16) | op.getValue().intValue());
            ops2.set(ops2.size() - 1, replacer);
            i = i2;
        }
    
        return i;
    }
    */
}
