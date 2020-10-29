package pt.up.fe.specs.binarytranslation.instruction.operand;

public enum OperandDataSize {

    // data sizes
    NIBBLE(4),
    BYTE(8),
    HALFWORD(16),
    WORD(32),
    DWORD(64),
    QWORD(128);

    private int value;

    private OperandDataSize(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
