package pt.up.fe.specs.binarytranslation.staticbinary;

import pt.up.fe.specs.binarytranslation.Instruction;

public interface ElfStream {

	Instruction nextInstruction();
}
