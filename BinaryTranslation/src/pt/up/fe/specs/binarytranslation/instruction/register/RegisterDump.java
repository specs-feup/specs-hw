package pt.up.fe.specs.binarytranslation.instruction.register;

import java.util.Map;

public interface RegisterDump {

    /*
     * get value by register definition (i.e. enum), such as MicroBlazeRegister.R1
     */
    public Number getValue(Register registerName);

    /*
     * get value by register string name, such as "r1"
     */
    // public Number getValue(String registerName);

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
