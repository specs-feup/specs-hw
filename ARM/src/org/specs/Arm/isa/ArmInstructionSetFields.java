package org.specs.Arm.isa;

import pt.up.fe.specs.binarytranslation.InstructionSetFields;
import pt.up.fe.specs.binarytranslation.InstructionType;

public enum ArmInstructionSetFields implements InstructionSetFields {

    add(0b000000, 1, InstructionType.add),
    mul(0b010000, 3, InstructionType.mul);

    /*
     * Instruction property fields
     */
    // private final int opcodebitmask =
    private final String instructionName;
    private final int opcode;
    private final int latency;
    private final InstructionType genericType;
    private final int nrInputs = 2; // TODO fix
    private final int nrOutputs = 1; // TODO fix
    private final boolean hasDelay = false;

    /*
     * Constructor
     */
    private ArmInstructionSetFields(int opcode, int latency, InstructionType tp) {
        this.instructionName = name();
        this.opcode = opcode;
        this.latency = latency;
        this.genericType = tp;
    }

    /*
     * Private helper method too look up the list
     */
    private int getLatency() {
        return this.latency;
    }

    /*
     * Private helper method too look up the list
     */
    private int getOpCode() {
        return this.opcode;
    }

    /*
     * Private helper method too look up the list
     */
    private InstructionType getGenericType() {
        return this.genericType;
    }

    /*
     * Initializes field in constructor for MicroBlazeInstruction
     */
    public static InstructionType getGenericType(int fullopcode) {
        int opcode = (fullopcode) >> (32 - 6);
        for (ArmInstructionSetFields insts : values()) {
            if (insts.getOpCode() == opcode)
                return insts.getGenericType();
        }
        return InstructionType.unknownType;
    }

    /*
     * Initializes field in constructor for MicroBlazeInstruction
     */
    public static int getLatency(int fullopcode) {
        int opcode = (fullopcode) >> (32 - 6);
        for (ArmInstructionSetFields insts : values()) {
            if (insts.getOpCode() == opcode)
                return insts.getLatency();
        }
        return -1; // TODO replace with exception
    }
}
