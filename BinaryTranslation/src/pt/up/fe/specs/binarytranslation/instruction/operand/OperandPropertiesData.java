package pt.up.fe.specs.binarytranslation.instruction.operand;

import java.util.List;

public interface OperandPropertiesData {

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
     * Width of register or immediate operand
     */
    public int getWidth();
}
