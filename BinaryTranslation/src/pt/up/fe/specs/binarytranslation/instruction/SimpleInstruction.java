package pt.up.fe.specs.binarytranslation.instruction;

import java.lang.reflect.Method;

public class SimpleInstruction {

    /*
     * Helper class to hold a dumbed down instruction
     * so as to consume less memory. Instructions can be rebuilt
     * by reparsing calling the constructor that originated them,
     * after frequent hashed sequences are determined
     */
    private String address;
    private String instruction;
    private Method c;

    public SimpleInstruction(Instruction i) {
        this.address = i.getAddress().toString();
        this.instruction = i.getInstruction();
        try {
            this.c = i.getClass().getMethod("newInstance", String.class, String.class);
        } catch (Exception e) {
            throw new RuntimeException(e.getCause());
        }
    }

    public Instruction rebuild() {
        Instruction i = null;
        try {
            i = (Instruction) this.c.invoke(null, this.address, this.instruction);
        } catch (Exception e) {
            throw new RuntimeException(e.getCause());
        }
        return i;
    }
}
