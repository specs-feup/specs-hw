package org.specs.Arm.isa;

import java.util.HashSet;

import pt.up.fe.specs.binarytranslation.asmparser.AsmInstructionData;
import pt.up.fe.specs.binarytranslation.generic.GenericIsaParser;
import pt.up.fe.specs.util.SpecsSystem;

public class ArmIsaParser extends GenericIsaParser {

    public ArmIsaParser() {
        super(ArmInstructionParsers.PARSERS, new HashSet<>(
                SpecsSystem.getStaticFields(ArmInstructionFields.class, String.class)));
    }

    @Override
    public ArmAsmInstructionData parse(String instruction) {

        AsmInstructionData fieldData = doparse(instruction);
        return new ArmAsmInstructionData(fieldData);
    }
}
