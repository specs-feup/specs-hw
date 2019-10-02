package org.specs.MicroBlaze.v2;

import java.math.BigInteger;

import org.specs.MicroBlaze.asmparser.MicroBlazeInstructionParsers;

import pt.up.fe.specs.binarytranslation.asmparser.IsaParser;
import pt.up.fe.specs.binarytranslation.generic.AInstruction;

public class MicroBlazeInstruction extends AInstruction {

    /*
     * 
     */
    public MicroBlazeInstruction(String address, String instruction) {
        super(Long.parseLong(address, 16), instruction);
        IsaParser parser = MicroBlazeInstructionParsers.getMicroBlazeIsaParser();
        this.fieldData = parser.parse(instruction);
        this.idata = MicroBlazeInstructionSet.process(fieldData);
        // lookup ISA table for static information
    }

    @Override
    public Number getBranchTarget() {
        if (this.isJump()) {
            int fullopcode = new BigInteger(this.getInstruction(), 16).intValue();
            short jmpval = (short) (fullopcode & 0x0000FFFF);
            return (this.getAddress().longValue() + (long) jmpval);
            // TODO replace mask with mask built based on elf instruction width
            // or info about instruction set
        }

        return null;
    }

    @Override
    public Number getInstructionCode() {
        return new BigInteger(this.getInstruction(), 16);
    }
}
