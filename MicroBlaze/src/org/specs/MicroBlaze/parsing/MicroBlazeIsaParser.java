package org.specs.MicroBlaze.parsing;

import pt.up.fe.specs.binarytranslation.asm.parsing.AIsaParser;
import pt.up.fe.specs.binarytranslation.asm.parsing.AsmFieldData;

public class MicroBlazeIsaParser extends AIsaParser {

    public MicroBlazeIsaParser() {
        super(MicroBlazeInstructionParsers.PARSERS, MicroBlazeAsmField.getFields());
    }

    @Override
    public MicroBlazeAsmFieldData parse(String addr, String instruction) {

        AsmFieldData fieldData = doparse(addr, instruction);
        return new MicroBlazeAsmFieldData(fieldData);
    }
}
