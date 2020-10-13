package pt.up.fe.specs.binarytranslation.stream.multistream;

import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.NullInstruction;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;
import pt.up.fe.specs.binarytranslation.stream.InstructionStreamType;
import pt.up.fe.specs.util.collections.concurrentchannel.ChannelConsumer;

public class InstructionStreamChannel implements InstructionStream {

    private final InstructionStream istream;
    private final ChannelConsumer<Instruction> channel;
    private Instruction currentInstruction, nextInstruction;

    public InstructionStreamChannel(InstructionStream istream, ChannelConsumer<Instruction> channel) {
        this.istream = istream;
        this.channel = channel;
        this.currentInstruction = null;
        this.nextInstruction = this.getnextInstruction();
    }

    @Override
    private Instruction getnextInstruction() {

        Instruction inst = null;
        try {
            inst = this.channel.take();

            // convert poison token to null
            if (inst.getInstruction() == null)
                return null;

        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return inst;

        /*Instruction inst = null;
        while (!this.istream.isClosed()) {
            inst = channel.poll();
            if (inst != null)
                break;
        }
        return inst;*/
    }

    @Override
    public Instruction nextInstruction() {

        if (nextInstruction == null) {
            return NullInstruction.NullInstance;
        }

        this.currentInstruction = this.nextInstruction;
        this.nextInstruction = this.getnextInstruction();
        this.numcycles += this.currentInstruction.getLatency();
        this.numinsts++;
        return this.currentInstruction;
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
        return;
    }

    @Override
    public boolean hasNext() {
        return this.nextInstruction != null;
    }

    @Override
    public void close() throws Exception {
        return;
    }

    @Override
    public boolean isClosed() {
        return this.hasNext();
    }
}
