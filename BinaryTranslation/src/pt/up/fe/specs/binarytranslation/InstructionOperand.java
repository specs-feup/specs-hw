package pt.up.fe.specs.binarytranslation;

public interface InstructionOperand {

    /*
     * Get value
     */
    public Integer getValue();

    /*
     * Get type (register or immediate)
     */
    public Integer getType();

    /*
     * Return formated string with <prefix><value>
     */
    public String getRepresentation();
}
