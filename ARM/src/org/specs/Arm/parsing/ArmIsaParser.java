package org.specs.Arm.parsing;

import pt.up.fe.specs.binarytranslation.parsing.AsmFieldData;
import pt.up.fe.specs.binarytranslation.parsing.GenericIsaParser;

public class ArmIsaParser extends GenericIsaParser {

    public ArmIsaParser() {
        super(ArmInstructionParsers.PARSERS, ArmAsmField.getFields());
    }

    @Override
    public ArmAsmFieldData parse(String instruction) {

        AsmFieldData fieldData = doparse(instruction);
        return new ArmAsmFieldData(fieldData);
    }
}
