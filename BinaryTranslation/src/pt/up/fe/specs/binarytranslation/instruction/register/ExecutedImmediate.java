package pt.up.fe.specs.binarytranslation.instruction.register;

import pt.up.fe.specs.binarytranslation.asm.parsing.AsmField;

public class ExecutedImmediate extends ExecutedRegister {

    public ExecutedImmediate(AsmField asmField, Number dataValue) {
        super(asmField, null, dataValue);
    }

    @Override
    public Register getRegisterDefinition() {
        return null;
    }
}
