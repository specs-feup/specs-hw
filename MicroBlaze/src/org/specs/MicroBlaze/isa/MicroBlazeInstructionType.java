package org.specs.MicroBlaze.isa;

import pt.up.fe.specs.binarytranslation.asmparser.AsmInstructionType;

public enum MicroBlazeInstructionType implements AsmInstructionType {

    SPECIAL,
    UBRANCH,
    CBRANCH,
    BARREL,
    IBARREL,
    RETURN,
    STREAM,
    DSTREAM,
    TYPE_A,
    TYPE_B,
    UNDEFINED
}
