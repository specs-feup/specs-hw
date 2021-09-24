package pt.up.fe.specs.binarytranslation.instruction.register;

import java.util.List;

public interface Register {

    public String getName();

    public List<RegisterType> getRegTypes();

    default boolean isProgramCounter() {
        return this.getRegTypes().contains(RegisterType.PROGRAMCOUNTER);
    }

    default boolean isParameter() {
        return this.getRegTypes().contains(RegisterType.PARAMETER);
    }

    default boolean isReturn() {
        return this.getRegTypes().contains(RegisterType.RETURNVALUE);
    }

    default boolean isStackPointer() {
        return this.getRegTypes().contains(RegisterType.STACKPOINTER);
    }

    default boolean isTemporary() {
        return this.getRegTypes().contains(RegisterType.TEMPORARY);
    }

    default boolean isZero() {
        return this.getRegTypes().contains(RegisterType.HARDZERO);
    }
}
