package pt.up.fe.specs.binarytranslation.generic;

import java.util.List;
import java.util.Map;

import pt.up.fe.specs.binarytranslation.InstructionSet;
import pt.up.fe.specs.binarytranslation.InstructionType;
import pt.up.fe.specs.binarytranslation.asmparser.AsmInstructionData;
import pt.up.fe.specs.binarytranslation.asmparser.AsmInstructionType;

public enum GenericInstructionSet implements InstructionSet {

    /*
     * Instruction property fields
     */
    private final String instructionName;
    private final int opcode; // 32 bit instruction code without operands
    private final int reducedopcode; // only the bits that matter, built after parsing the fields
    private final int latency;
    private final int delay;
    private final AsmInstructionType codetype;
    private final List<InstructionType> genericType;
    private final AsmInstructionData iData; // decoded fields of this instruction

    /*
     * Creates a table of lists, where each type of instruction
     *  format gets its list of instructions in that format
     */
    private static Map<AsmInstructionType, List<InstructionSet>> typeLists = makeTypeLists();

    /*
     * Constructor
     */
    private GenericInstructionSet(int opcode, int latency,
            int delay, AsmInstructionType mbtype, InstructionType... tp) {

        this.opcode = opcode;
        this.latency = latency;
        this.delay = delay;
        this.codetype = mbtype;
        this.genericType = Arrays.asList(tp);

        // protected??
        this.instructionName = name();

        // use the parser to initialize private fields of instruction set itself
        IsaParser parser = MicroBlazeInstructionParsers.getMicroBlazeIsaParser();
        this.iData = parser.parse(Integer.toHexString(opcode)); // TODO make new overload for "parse"
        this.reducedopcode = this.iData.getReducedOpcode();
    }

}
