package org.specs.MicroBlaze.parsing;

import pt.up.fe.specs.binarytranslation.asm.parsing.AIsaParser;

public class MicroBlazeIsaParser extends AIsaParser {

    public MicroBlazeIsaParser() {
        super(MicroBlazeInstructionParsers.PARSERS, MicroBlazeAsmField.getFields());
    }

    @Override
    public MicroBlazeAsmFieldData parse(String addr, String instruction) {

        // TODO: find out a way to make "AsmFieldData" class abstract!
        return new MicroBlazeAsmFieldData(doparse(addr, instruction));
    }
}
