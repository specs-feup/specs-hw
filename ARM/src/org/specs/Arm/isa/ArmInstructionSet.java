package org.specs.Arm.isa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pt.up.fe.specs.binarytranslation.InstructionType;

public enum ArmInstructionSet {

    add(0b000000, 1, InstructionType.G_ADD),
    mul(0b010000, 3, InstructionType.G_MUL);

    /*
     * Instruction property fields
     */
    // private final int opcodebitmask =
    private final String instructionName;
    private final int opcode;
    private final int latency;
    private final List<InstructionType> genericType;

    // private final int nrInputs = 2; // TODO fix
    // private final int nrOutputs = 1; // TODO fix
    // private final boolean hasDelay = false;

    /*
     * Constructor
     */
    private ArmInstructionSet(int opcode, int latency, InstructionType tp) {
        this.instructionName = name();
        this.opcode = opcode;
        this.latency = latency;
        this.genericType = Arrays.asList(tp);
    }

    /*
     * Private helper method too look up the list
     */
    private int getLatency() {
        return this.latency;
    }

    /*
     * Private helper method too look up opcode in the list
     */
    private int getOpCode() {
        return this.opcode;
    }

    /*
     * Private helper method too look up type in the list
     */
    private List<InstructionType> getGenericType() {
        return this.genericType;
    }

    /*
     * Private helper method too look up name the list
     */
    private String getName() {
        return this.instructionName;
    }

    /*
     * Initializes field in constructor for MicroBlazeInstruction
     */
    public static String getName(long fullopcode) {
        long opcode = (fullopcode) >> (32 - 6);
        for (ArmInstructionSet insts : values()) {
            if (insts.getOpCode() == opcode)
                return insts.getName();
        }
        return "Unknown Instruction!";
        // TODO throw something here
    }

    /*
     * Initializes field in constructor for MicroBlazeInstruction
     */
    public static List<InstructionType> getGenericType(long fullopcode) {
        long opcode = (fullopcode) >> (32 - 6);
        for (ArmInstructionSet insts : values()) {
            if (insts.getOpCode() == opcode)
                return insts.getGenericType();
        }
        // return InstructionType.unknownType;
        List<InstructionType> ret = new ArrayList<InstructionType>();
        ret.add(InstructionType.G_UNKN);
        return ret;
    }

    /*
     * Initializes field in constructor for MicroBlazeInstruction
     */
    public static int getLatency(long fullopcode) {
        long opcode = (fullopcode) >> (32 - 6);
        for (ArmInstructionSet insts : values()) {
            if (insts.getOpCode() == opcode)
                return insts.getLatency();
        }
        return -1; // TODO replace with exception
    }
}
