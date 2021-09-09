package pt.up.fe.specs.binarytranslation.instruction;

/*
 * Init this with one of the "newInstance" methods
 */
public interface InstructionInstantiator {

    Instruction apply(String name, String addr, String rawRegisterDump);
}
