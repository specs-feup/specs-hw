package org.specs.MicroBlaze.isa;

import java.util.HashSet;

import pt.up.fe.specs.binarytranslation.parsing.AsmFieldData;
import pt.up.fe.specs.binarytranslation.parsing.GenericIsaParser;
import pt.up.fe.specs.util.SpecsSystem;

public class MicroBlazeIsaParser extends GenericIsaParser {

    public MicroBlazeIsaParser() {
        super(MicroBlazeInstructionParsers.PARSERS, new HashSet<>(
                SpecsSystem.getStaticFields(MicroBlazeAsmFields.class, String.class)));
    }

    @Override
    public MicroBlazeAsmFieldData parse(String instruction) {

        AsmFieldData fieldData = doparse(instruction);
        return new MicroBlazeAsmFieldData(fieldData);
    }
}
