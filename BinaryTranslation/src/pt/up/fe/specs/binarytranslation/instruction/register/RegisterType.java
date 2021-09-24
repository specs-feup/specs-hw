package pt.up.fe.specs.binarytranslation.instruction.register;

public enum RegisterType {

    PROGRAMCOUNTER,
    GENERALPURPOSE,
    TEMPORARY,
    STACKPOINTER,

    RETURNADDR,
    GLOBALPOINTER, // specific to riscv?
    THREADPOINTER,
    SAVED, // "saved registers" in riscv...

    RETURNVALUE,
    PARAMETER,
    HARDZERO,
    SPECIAL
}
