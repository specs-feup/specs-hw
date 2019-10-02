package pt.up.fe.specs.binarytranslation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.up.fe.specs.binarytranslation.asmparser.AsmInstructionData;
import pt.up.fe.specs.binarytranslation.asmparser.AsmInstructionType;
import pt.up.fe.specs.binarytranslation.generic.InstructionData;

public interface InstructionSet {

    /*
     * Private helper method too look up the list
     */
    abstract int getLatency();

    /*
     * Private helper method too look up the list
     */
    abstract int getDelay();

    /*
     * Private helper method too get full opcode
     */
    abstract int getOpCode();

    /*
     * Private helper method too get only the bits that matter
     */
    abstract int getReducedOpCode();

    /*
     * Private helper method too look up type of instruction format
     */
    abstract AsmInstructionType getCodeType();

    /*
     * Private helper method too look up type in the list
     */
    abstract List<InstructionType> getGenericType();

    /*
     * Private helper method too look up name the list
     */
    abstract String getName();

    /*
     * Gets only the instructions which match format of type "type"
     */
    abstract List<InstructionSet> getTypeList(AsmInstructionType type);

    /*
     * Constructs a map where each key is a type of 
     * instruction format, and the list are the instructions of that format
     */
    public static Map<AsmInstructionType, List<InstructionSet>> makeTypeLists(AsmInstructionType[] types,
            InstructionSet[] insts) {

        var ret = new HashMap<AsmInstructionType, List<InstructionSet>>();

        // For each instruction format
        for (AsmInstructionType type : types) {
            var nlist = new ArrayList<InstructionSet>();

            // For each instruction in the set, which fits that format, make the list
            for (InstructionSet inst : insts) {
                if (inst.getCodeType() == type) {
                    nlist.add(inst);
                }
            }
            ret.put(type, nlist);
        }
        return ret;
    }

    /*
     * Initializes field in constructor for MicroBlazeInstruction
     */
    private String getName(AsmInstructionData asmData) {
        for (InstructionSet inst : getTypeList(asmData.getType())) {
            if (inst.getReducedOpCode() == asmData.getReducedOpcode())
                return inst.getName();
        }
        return "Unknown Instruction!";
        // TODO throw something here
    }

    /*
     * Initializes field in constructor for MicroBlazeInstruction
     */
    private List<InstructionType> getGenericType(AsmInstructionData asmData) {
        for (InstructionSet inst : getTypeList(asmData.getType())) {
            if (inst.getReducedOpCode() == asmData.getReducedOpcode())
                return inst.getGenericType();
        }
        // return InstructionType.unknownType;
        List<InstructionType> ret = new ArrayList<InstructionType>();
        ret.add(InstructionType.G_UNKN);
        return ret;
    }

    /*
     * Initializes field in constructor for MicroBlazeInstruction
     */
    private int getLatency(AsmInstructionData asmData) {
        for (InstructionSet inst : getTypeList(asmData.getType())) {
            if (inst.getReducedOpCode() == asmData.getReducedOpcode())
                return inst.getLatency();
        }
        return -1; // TODO replace with exception
    }

    /*
     * Initializes field in constructor for MicroBlazeInstruction
     */
    private int getDelay(AsmInstructionData asmData) {
        for (InstructionSet inst : getTypeList(asmData.getType())) {
            if (inst.getReducedOpCode() == asmData.getReducedOpcode())
                return inst.getDelay();
        }
        return -1; // TODO replace with exception
    }

    /*
     * Interpret field data given by parsers
     */
    public default InstructionData process(AsmInstructionData fieldData) {

        String plainname = getName(fieldData);
        int latency = getLatency(fieldData);
        int delay = getDelay(fieldData);
        List<InstructionType> genericTypes = getGenericType(fieldData);
        // TODO operands

        return new InstructionData(plainname, latency, delay, genericTypes);
    }
}
