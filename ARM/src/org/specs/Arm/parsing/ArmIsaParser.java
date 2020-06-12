package org.specs.Arm.parsing;

import pt.up.fe.specs.binarytranslation.parsing.AsmFieldData;
import pt.up.fe.specs.binarytranslation.parsing.AIsaParser;

public class ArmIsaParser extends AIsaParser {

    public ArmIsaParser() {
        super(ArmInstructionParsers.PARSERS, ArmAsmField.getFields());
    }

    @Override
    public ArmAsmFieldData parse(String addr, String instruction) {

        AsmFieldData fieldData = doparse(addr, instruction);
        return new ArmAsmFieldData(fieldData);
    }
}
