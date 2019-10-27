package org.specs.MicroBlaze.parsing;

import pt.up.fe.specs.binarytranslation.parsing.AsmFieldData;
import pt.up.fe.specs.binarytranslation.parsing.GenericIsaParser;

public class MicroBlazeIsaParser extends GenericIsaParser {

    public MicroBlazeIsaParser() {
        super(MicroBlazeInstructionParsers.PARSERS, MicroBlazeAsmFields.getFields());
    }

    @Override
    public MicroBlazeAsmFieldData parse(String instruction) {

        AsmFieldData fieldData = doparse(instruction);
        return new MicroBlazeAsmFieldData(fieldData);
    }
}
