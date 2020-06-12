package pt.up.fe.specs.binarytranslation.instruction.operand;

import java.util.List;

import pt.up.fe.specs.binarytranslation.parsing.AsmField;

/**
 * Each operand in an ISA is defined by properties. This class is to be implemented by an enum per ISA, which lists all
 * the operand types in the set, and implements the methods to retrieve properties about each operand type.
 * 
 * @author Nuno Paulino
 *
 */
public interface OperandProperties {

    /*
     * returns generic operandtype
     */
    public List<OperandType> getTypes();

    /*
     * Should return a main archetype (i.e. REGISTER or IMMEDIATE)
     */
    public OperandType getMainType();

    /*
     * Gets the human readable prefix of the operand, e.g. "r", or "q"
     */
    public String getPrefix();

    /*
     * Gets the human readable suffix of the operand
     */
    public String getSuffix();

    /*
     * 
     */
    public String getSymbolicPrefix();

    /*
     * 
     */
    public String getSymbolicSuffix();

    /*
     * Width of register or immediate operand
     */
    public int getWidth();

    /*
     * 
     */
    public AsmField getAsmField();

    /*
     * 
     */
    public void setSymbolic();

    /*
     * Copy method
     */
    public OperandProperties copy();
}
