package pt.up.fe.specs.binarytranslation;

public interface InstructionSetFields {

    /*
     * Check if instruction is addition
     */
    public boolean isAdd();

    /*
     * Check if instruction is subtraction
     */
    public boolean isSub();

    /*
     * Check if instruction is logical
     */
    public boolean isLogical();

    /*
     * Check if instruction is unary logical
     */
    public boolean isUnaryLogical();

    /*
     * Check if instruction is jump (of any kind)
     */
    default public boolean isJump() {
        return (isConditionalJump() | isUnconditionalJump());
    }

    /*
     * Check if instruction is logical
     */
    public boolean isConditionalJump();

    /*
     * Check if instruction is logical
     */
    public boolean isUnconditionalJump();

    /*
     * Check if instruction is store
     */
    public boolean isStore();

    /*
     * Check if instruction is load
     */
    public boolean isLoad();

    /*
     * Check if instruction is memory access
     */
    default public boolean isMemory() {
        return (isStore() | isLoad());
    }
}
