package pt.up.fe.specs.binarytranslation;

public enum InstructionType {

    /*
     * Generic instruction types.
     * Specific architecture implementations 
     * must map each instruction in the ISA
     * to one of these types 
     */
    add,
    sub,
    mul,
    logical,
    unarylogical,
    jump,
    cjump,
    ujump,
    store,
    load,
    memory,
    floatingpoint,
    other,
    unknownType // TODO should fire off an exception
    // TODO add more types
}
