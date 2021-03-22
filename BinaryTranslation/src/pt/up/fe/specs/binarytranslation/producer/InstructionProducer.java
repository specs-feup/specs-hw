package pt.up.fe.specs.binarytranslation.producer;

import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.NullInstruction;
import pt.up.fe.specs.binarytranslation.producer.detailed.RegisterDump;
import pt.up.fe.specs.util.threadstream.ObjectProducer;

public interface InstructionProducer extends ObjectProducer<Instruction> {

    @Override
    default Instruction getPoison() {
        return NullInstruction.NullInstance;
    }

    /**
     * 
     * @return the next instruction of the stream, or null if there are no more instructions in the stream
     */
    public Instruction nextInstruction();

    /**
     * Only implementable by a @TraceInstructionProducer which is executing a simulator @ProcessRun
     */
    default public RegisterDump queryRegisters() {
        return null;
    }

    /**
     * 
     * @return
     */
    public Integer getInstructionWidth();

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
