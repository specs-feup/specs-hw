package org.specs.Arm.isa;

import pt.up.fe.specs.binarytranslation.instruction.InstructionCondition;

/*
 * Predicates for the execution of ARM instructions which contain a "cond" field
 */
public enum ArmInstructionCondition implements InstructionCondition {

    Equal("eq", 0b0000),
    NotEqual("ne", 0b0001),
    CarrySet("cs", 0b0010),

    CarryClear("cc", 0b0011),
    MinusNegative("mn", 0b0100),
    PlusPositiveZero("pl", 0b0101),

    Overflow("vs", 0b0110),
    NoOverflow("vc", 0b0111),
    UnsignedHigher("hi", 0b1000),

    UnsignedLowerSame("ls", 0b1001),
    SignedGreaterThanorEqual("ge", 0b1010),
    SignedLessThan("lt", 0b1011),

    SignedGreaterThan("gt", 0b1100),
    SignedLessThanorEqual("le", 0b1101),
    Always1("al", 0b1110),
    Always2("nvb", 0b1111),

    NONE("NONE", 0b0000);

    private String shorthandle;
    private int condCode;

    private ArmInstructionCondition(String shorthandle, int condCode) {
        this.shorthandle = shorthandle;
        this.condCode = condCode;
    }

    public Boolean isConditional() {
        return this.equals(ArmInstructionCondition.NONE);
    }

    public int getCondCode() {
        return condCode;
    }

    public String getShorthandle() {
        return shorthandle;
    }
}
