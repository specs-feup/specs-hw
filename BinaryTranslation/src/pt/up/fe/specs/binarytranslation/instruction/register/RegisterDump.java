package pt.up.fe.specs.binarytranslation.instruction.register;

import java.util.Map;

import pt.up.fe.specs.binarytranslation.asm.parsing.AsmField;

public interface RegisterDump {

    /*
     * 
     */
    public Number getValue(AsmField fieldName);

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
    public Map<AsmField, Number> getRegisterMap();
}
