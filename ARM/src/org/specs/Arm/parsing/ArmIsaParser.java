package org.specs.Arm.parsing;

import java.util.HashSet;

import org.specs.Arm.instruction.ArmInstructionFields;

import pt.up.fe.specs.binarytranslation.parsing.AsmFieldData;
import pt.up.fe.specs.binarytranslation.parsing.GenericIsaParser;
import pt.up.fe.specs.util.SpecsSystem;

public class ArmIsaParser extends GenericIsaParser {

    public ArmIsaParser() {
        super(ArmInstructionParsers.PARSERS, new HashSet<>(
                SpecsSystem.getStaticFields(ArmInstructionFields.class, String.class)));
    }

    @Override
    public ArmAsmFieldData parse(String instruction) {

        AsmFieldData fieldData = doparse(instruction);
        return new ArmAsmFieldData(fieldData);
    }
}
