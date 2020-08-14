package org.specs.Arm.instruction;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import pt.up.fe.specs.binarytranslation.instruction.InstructionPseudocode;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionLexer;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.PseudoInstructionContext;

public enum ArmPseudocode implements InstructionPseudocode {

    add("RD = RN + IMM;"),

    ubfm("RD = RN << (IMMS + 1);"),

    ands_shift_reg("ands", "RD = RN & (RM << IMM); $nzvc = (RD == 0);"),

    // beq("b.eq", "if($PSTATE[0] == 1) $PC = $PC + sext(IMM);"),
    // beq("b.eq", "if($PSTATE == 1) $PC = $PC + IMM;"),
    beq("b.eq", "$PC = $PC + (IMM * ($nzvc == 1));"),

    defaultCode("RD = RA;"); // i.e. nop

    private final String instName;
    private final String pseudocode;

    private ArmPseudocode(String pseudocode) {
        this.pseudocode = pseudocode;
        this.instName = name();
    }

    private ArmPseudocode(String name, String pseudocode) {
        this.instName = name;
        this.pseudocode = pseudocode;
    }

    public String getName() {
        return instName;
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
