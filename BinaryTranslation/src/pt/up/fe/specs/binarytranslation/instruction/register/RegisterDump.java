package pt.up.fe.specs.binarytranslation.instruction.register;

import java.util.Map;

public interface RegisterDump {

    /*
     * 
     */
    public Number getValue(Register registerName);

    /*
     * 
     */
    public void prettyPrint();

    /*
     * 
     */
    public boolean isEmpty();

    /*
     * 
     */
    public Map<Register, Number> getRegisterMap();

    /*
     * 
     */
    public RegisterDump copy();
}
