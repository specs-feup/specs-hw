package pt.up.fe.specs.binarytranslation.instruction.register;

import java.util.Map;

public class RegisterDumpNull implements RegisterDump {

    public RegisterDumpNull() {
    }

    @Override
    public Number getValue(Register registerName) {
        return 0;
    }

    @Override
    public void prettyPrint() {
        System.out.println("Register dump is empty");
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public Map<Register, Number> getRegisterMap() {
        return null;
    }

    @Override
    public RegisterDump copy() {
        return this;
    }
}
