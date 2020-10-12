package pt.up.fe.specs.binarytranslation.stream.multistream;

import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;
import pt.up.fe.specs.binarytranslation.stream.InstructionStreamType;
import pt.up.fe.specs.util.collections.concurrentchannel.ChannelConsumer;

public class InstructionStreamChannel implements InstructionStream {

    private final InstructionStream istream;
    private final ChannelConsumer<Instruction> channel;

    public InstructionStreamChannel(InstructionStream istream, ChannelConsumer<Instruction> channel) {
        this.istream = istream;
        this.channel = channel;
    }

    @Override
    public void close() throws Exception {
        this.istream.close();
    }

    // TODO: correct???

    @Override
    public Instruction nextInstruction() {
        try {
            return channel.take();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public InstructionStreamType getType() {
        return this.istream.getType();
    }

    @Override
    public Application getApplicationInformation() {
        return this.istream.getApplicationInformation();
    }

    @Override
    public int getInstructionWidth() {
        return this.istream.getInstructionWidth();
    }

    @Override
    public void rawDump() {
        // ???
    }

    // TODO: bug here since the original stream might be empty, but this local channel might still have content
    @Override
    public boolean hasNext() {
        return (this.istream.hasNext());
    }
}
