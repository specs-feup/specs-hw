package org.specs.Arm.isa;

public enum ArmInstructionConditions {

    NONE("NONE", 0b0000),

    Equal("EQ", 0b0000),
    NotEqual("NE", 0b0001),
    CarrySet("CS", 0b0010),

    CarryClear("CC", 0b0011),
    MinusNegative("MN", 0b0100),
    PlusPositiveZero("PL", 0b0101),

    Overflow("VS", 0b0110),
    NoOverflow("VC", 0b0111),
    UnsignedHigher("HI", 0b1000),

    UnsignedLowerSame("LS", 0b1001),
    SignedGreaterThanorEqual("GE", 0b1010),
    SignedLessThan("LT", 0b1011),

    SignedGreaterThan("GT", 0b1100),
    SignedLessThanorEqual("LE", 0b1101),
    Always1("AL", 0b1110),
    Always2("NVb", 0b1111);

    private String shorthandle;
    private int condCode;

    private ArmInstructionConditions(String shorthandle, int condCode) {
        this.shorthandle = shorthandle;
        this.condCode = condCode;
    }

    public int getCondCode() {
        return condCode;
    }

    public String getShorthandle() {
        return shorthandle;
    }
}
