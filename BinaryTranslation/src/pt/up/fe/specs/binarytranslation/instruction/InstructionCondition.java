package pt.up.fe.specs.binarytranslation.instruction;

/*
 * Predicates for the execution of instructions
 */
public interface InstructionCondition {

    public Boolean isConditional();

    public int getCondCode();

    public String getShorthandle();
}
