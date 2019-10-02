package pt.up.fe.specs.binarytranslation;

import java.util.List;

import pt.up.fe.specs.binarytranslation.asmparser.AsmInstructionType;

public interface InstructionProperties {

    /*
     * Private helper method too look up the list
     */
    public int getLatency();

    /*
     * Private helper method too look up the list
     */
    public int getDelay();

    /*
     * Private helper method too get full opcode
     */
    public int getOpCode();

    /*
     * Private helper method too get only the bits that matter
     */
    public int getReducedOpCode();

    /*
     * Private helper method too look up type of instruction format
     */
    public AsmInstructionType getCodeType();

    /*
     * Private helper method too look up type in the list
     */
    public List<InstructionType> getGenericType();

    /*
     * Private helper method too look up name the list
     */
    public String getName();
}
