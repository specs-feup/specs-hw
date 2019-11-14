package org.specs.MicroBlaze.parsing;

import pt.up.fe.specs.binarytranslation.parsing.AsmFieldData;
import pt.up.fe.specs.binarytranslation.parsing.GenericIsaParser;

public class MicroBlazeIsaParser extends GenericIsaParser {

    public MicroBlazeIsaParser() {
        super(MicroBlazeInstructionParsers.PARSERS, MicroBlazeAsmField.getFields());
    }

    @Override
    public MicroBlazeAsmFieldData parse(String addr, String instruction) {

        AsmFieldData fieldData = doparse(addr, instruction);
        return new MicroBlazeAsmFieldData(fieldData);
    }
}
