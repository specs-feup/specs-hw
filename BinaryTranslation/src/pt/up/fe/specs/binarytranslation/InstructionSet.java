package pt.up.fe.specs.binarytranslation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.up.fe.specs.binarytranslation.asmparser.AsmInstructionData;
import pt.up.fe.specs.binarytranslation.asmparser.AsmInstructionType;
import pt.up.fe.specs.binarytranslation.generic.InstructionData;

public class InstructionSet {

    private List<InstructionProperties> instList;
    private List<AsmInstructionType> codeTypes;
    private Map<AsmInstructionType, List<InstructionProperties>> typeLists;

    /*
     * Constructor of instruction set; set is constructed from list of instruction
     * implemented as an enum, which implements InstructionProperties
     */
    public InstructionSet(List<InstructionProperties> instList, List<AsmInstructionType> codeTypes) {
        this.instList = instList;
        this.codeTypes = codeTypes;
        this.typeLists = makeTypeLists();
    }

    /*
     * Gets only the instructions which match format of type "type"
     */
    private List<InstructionProperties> getTypeList(AsmInstructionType type) {
        return this.typeLists.get(type);
    }

    /*
     * Constructs a map where each key is a type of 
     * instruction format, and the list are the instructions of that format
     */
    private Map<AsmInstructionType, List<InstructionProperties>> makeTypeLists() {

        var ret = new HashMap<AsmInstructionType, List<InstructionProperties>>();

        // For each instruction format
        for (AsmInstructionType codetype : codeTypes) {
            var nlist = new ArrayList<InstructionProperties>();

            // For each instruction in the set, which fits that format, make the list
            for (InstructionProperties inst : instList) {
                if (inst.getCodeType() == codetype) {
                    nlist.add(inst);
                }
            }
            ret.put(codetype, nlist);
        }
        return ret;
    }

    /*
     * Initializes field in constructor for MicroBlazeInstruction
     */
    private String getName(AsmInstructionData asmData) {
        for (InstructionProperties inst : getTypeList(asmData.getType())) {
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
        for (InstructionProperties inst : getTypeList(asmData.getType())) {
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
        for (InstructionProperties inst : getTypeList(asmData.getType())) {
            if (inst.getReducedOpCode() == asmData.getReducedOpcode())
                return inst.getLatency();
        }
        return -1; // TODO replace with exception
    }

    /*
     * Initializes field in constructor for MicroBlazeInstruction
     */
    private int getDelay(AsmInstructionData asmData) {
        for (InstructionProperties inst : getTypeList(asmData.getType())) {
            if (inst.getReducedOpCode() == asmData.getReducedOpcode())
                return inst.getDelay();
        }
        return -1; // TODO replace with exception
    }

    /*
     * Interpret field data given by parsers
     */
    public InstructionData process(AsmInstructionData fieldData) {

        String plainname = getName(fieldData);
        int latency = getLatency(fieldData);
        int delay = getDelay(fieldData);
        List<InstructionType> genericTypes = getGenericType(fieldData);
        // TODO operands

        return new InstructionData(plainname, latency, delay, genericTypes);
    }
}
