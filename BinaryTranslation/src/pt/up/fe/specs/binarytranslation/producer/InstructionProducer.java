package pt.up.fe.specs.binarytranslation.producer;

import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.NullInstruction;
import pt.up.fe.specs.binarytranslation.utils.replicator.ObjectProducer;

public interface InstructionProducer extends ObjectProducer<Instruction> {

    @Override
    default Instruction getPoison() {
        return NullInstruction.NullInstance;
    }

    /**
     * 
     * @return the next instruction of the stream, or null if there are no more instructions in the stream
     */
    Instruction nextInstruction();

    /**
     * Outputs the unprocessed incoming stream
     */
    public void rawDump();

    /**
     * 
     * @return The final {@ApplicationInformation} object containing appname, compile info, and cpu architecture
     */
    public Application getApp();
}
