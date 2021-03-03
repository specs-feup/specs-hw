package pt.up.fe.specs.binarytranslation.producer.detailed;

public class RegisterDumpNull extends RegisterDump {
    
    public RegisterDumpNull() {}
    
    @Override
    public void prettyPrint() {
        System.out.println("Register dump is empty");
    }
}
