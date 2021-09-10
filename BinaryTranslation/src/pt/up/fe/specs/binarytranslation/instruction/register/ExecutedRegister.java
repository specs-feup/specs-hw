package pt.up.fe.specs.binarytranslation.instruction.register;

import pt.up.fe.specs.binarytranslation.asm.parsing.AsmField;

public class ExecutedRegister {

    private final AsmField asmField;
    private final Register registerDefinition;
    private final Number dataValue;

    public ExecutedRegister(AsmField asmField, Register regdef, Number dataValue) {
        this.asmField = asmField;
        this.registerDefinition = regdef;
        this.dataValue = dataValue;
    }

    public AsmField getAsmField() {
        return asmField;
    }

    public Number getDataValue() {
        return dataValue;
    }

    public Register getRegisterDefinition() {
        return registerDefinition;
    }
}
