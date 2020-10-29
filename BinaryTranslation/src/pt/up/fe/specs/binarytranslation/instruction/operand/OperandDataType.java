package pt.up.fe.specs.binarytranslation.instruction.operand;

public enum OperandDataType {

    // special registers?
    BITFIELD,

    // scalars
    SCALAR_INTEGER,
    SCALAR_FLOAT,

    // simds
    SIMD_INTEGER,
    SIMD_FLOAT

    // TODO: operands can be both simd and interger and or float...
}
