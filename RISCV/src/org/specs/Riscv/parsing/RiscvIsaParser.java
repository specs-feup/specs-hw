package org.specs.Riscv.parsing;

import pt.up.fe.specs.binarytranslation.parsing.AIsaParser;
import pt.up.fe.specs.binarytranslation.parsing.AsmFieldData;

public class RiscvIsaParser extends AIsaParser {

    public RiscvIsaParser() {
        super(RiscvInstructionParsers.PARSERS, RiscvAsmField.getFields());
    }

    @Override
    public RiscvAsmFieldData parse(String addr, String instruction) {

        AsmFieldData fieldData = doparse(addr, instruction);
        return new RiscvAsmFieldData(fieldData);
    }
}
