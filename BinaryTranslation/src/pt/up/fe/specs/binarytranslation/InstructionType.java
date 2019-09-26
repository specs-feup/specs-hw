package pt.up.fe.specs.binarytranslation;

public enum InstructionType {

    /*
     * Generic instruction types.
     * Specific architecture implementations 
     * must map each instruction in the ISA
     * to one of these types 
     */
    G_ADD,
    G_SUB,
    G_MUL,
    G_LOGICAL,
    G_UNARY,
    G_JUMP, // is jump
    G_CJUMP, // jump is conditional
    G_UJUMP, // jump is unconditional
    G_RJUMP, // jump is relative
    G_AJUMP, // jump is absolute
    G_IJUMP, // jump with immediate value
    G_STORE,
    G_LOAD,
    G_MEMORY,
    G_FLOAT,
    G_OTHER,
    G_UNKN // TODO should fire off an exception
    // TODO add more types
}
