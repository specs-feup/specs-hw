package pt.up.fe.specs.binarytranslation.instruction;

import pt.up.fe.specs.binarytranslation.producer.detailed.RegisterDump;

/*
 * Init this with one of the "newInstance" methods
 */
public interface InstructionInstantiator {

    Instruction apply(String name, String addr, RegisterDump registers);
}
