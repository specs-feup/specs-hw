package pt.up.fe.specs.binarytranslation.stream.multistream;

import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.NullInstruction;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;
import pt.up.fe.specs.binarytranslation.stream.InstructionStreamType;
import pt.up.fe.specs.util.collections.concurrentchannel.ChannelConsumer;
import pt.up.fe.specs.util.threadstream.GenericObjectStream;

public class InstructionStreamChannel extends GenericObjectStream<Instruction> implements InstructionStream {

    private final InstructionStream istream;

    public InstructionStreamChannel(InstructionStream istream, ChannelConsumer<Instruction> channel) {
        super(channel, NullInstruction.NullInstance);
        this.istream = istream;
    }

    @Override
    public Instruction nextInstruction() {
        return this.next();
    }

    @Override
    public InstructionStreamType getType() {
        return this.istream.getType();
    }

    @Override
    public int getInstructionWidth() {
        return this.istream.getInstructionWidth();
    }

    @Override
    public long getNumInstructions() {
        return this.istream.getNumInstructions();
    }

    @Override
    public long getCycles() {
        return this.istream.getCycles();
    }

    @Override
    public Application getApp() {
        return this.istream.getApp();
    }
}
