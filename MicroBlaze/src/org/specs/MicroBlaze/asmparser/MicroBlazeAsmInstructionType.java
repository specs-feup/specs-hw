package org.specs.MicroBlaze.asmparser;

import pt.up.fe.specs.binarytranslation.asmparser.AsmInstructionType;

public enum MicroBlazeAsmInstructionType implements AsmInstructionType {

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
