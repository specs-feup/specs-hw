package org.specs.MicroBlaze.isa;

import java.util.HashSet;

import pt.up.fe.specs.binarytranslation.asmparser.AsmInstructionData;
import pt.up.fe.specs.binarytranslation.generic.GenericIsaParser;
import pt.up.fe.specs.util.SpecsSystem;

public class MicroBlazeIsaParser extends GenericIsaParser {

    public MicroBlazeIsaParser() {
        super(MicroBlazeInstructionParsers.PARSERS, new HashSet<>(
                SpecsSystem.getStaticFields(MicroBlazeInstructionFields.class, String.class)));
    }

    @Override
    public AsmInstructionData parse(String instruction) {
        return doparse(instruction);
    }
}
