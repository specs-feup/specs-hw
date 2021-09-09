package pt.up.fe.specs.binarytranslation.instruction.register;

public class RegisterDumpNull extends RegisterDump {
    
    public RegisterDumpNull() {}
    
    @Override
    public void prettyPrint() {
        System.out.println("Register dump is empty");
    }
}
