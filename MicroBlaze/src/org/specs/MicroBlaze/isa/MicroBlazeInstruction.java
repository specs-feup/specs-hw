package org.specs.MicroBlaze.isa;

import java.math.BigInteger;
import java.util.Arrays;

import pt.up.fe.specs.binarytranslation.InstructionSet;
import pt.up.fe.specs.binarytranslation.generic.AInstruction;

public class MicroBlazeInstruction extends AInstruction {

    /*
     * Parser and "decoder" Shared by all
     */
    static {
        instSet = new InstructionSet(Arrays.asList(MicroBlazeInstructionProperties.values()),
                Arrays.asList(MicroBlazeInstructionType.values()));
        parser = MicroBlazeInstructionParsers.getMicroBlazeIsaParser();
    }

    /*
     * Create the instruction
     */
    public MicroBlazeInstruction(String address, String instruction) {
        super(Long.parseLong(address, 16), instruction);
        this.fieldData = parser.parse(instruction);
        this.idata = instSet.process(fieldData);
        // lookup ISA table for static information

        // TODO trim non used operands based on specifics of Microblaze ISA here?
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
}
