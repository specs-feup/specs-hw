package pt.up.fe.specs.binarytranslation.instruction.register;

import java.util.Map;

enum RegisterDumpNull implements RegisterDump {

    NullInstance;

    @Override
    public Number getValue(Register registerName) {
        return null;
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
