package org.specs.Arm.instruction;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import pt.up.fe.specs.binarytranslation.instruction.InstructionPseudocode;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionLexer;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.PseudoInstructionContext;

public enum ArmPseudocode implements InstructionPseudocode {

    add("RD = RN + IMM;"),

    defaultCode("RD = RA;"); // i.e. nop

    private final String name;
    private final String pseudocode;

    private ArmPseudocode(String pseudocode) {
        this.pseudocode = pseudocode;
        this.name = name();
    }

    public String getName() {
        return name;
    }

    @Override
    public String getCode() {
        return pseudocode;
    }

    @Override
    public PseudoInstructionContext getParseTree() {
        var parser = new PseudoInstructionParser(
                new CommonTokenStream(new PseudoInstructionLexer(new ANTLRInputStream(this.getCode()))));
        return parser.pseudoInstruction();
    }

    /*
     * Using this for now to deal with unimplemented codes!
     */
    public static InstructionPseudocode getDefault() {
        return ArmPseudocode.defaultCode;
    }
}
