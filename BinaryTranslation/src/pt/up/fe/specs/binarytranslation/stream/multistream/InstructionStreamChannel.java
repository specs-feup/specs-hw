package pt.up.fe.specs.binarytranslation.stream.multistream;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.NullInstruction;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;
import pt.up.fe.specs.binarytranslation.stream.InstructionStreamType;
import pt.up.fe.specs.util.collections.concurrentchannel.ChannelConsumer;

public class InstructionStreamChannel implements InstructionStream {

    private boolean inited = false;
    private boolean isClosed = false;
    private final InstructionStream istream;
    private final ChannelConsumer<Instruction> channel;
    private Instruction currentInstruction, nextInstruction;

    public InstructionStreamChannel(InstructionStream istream, ChannelConsumer<Instruction> channel) {
        this.istream = istream;
        this.channel = channel;
        this.currentInstruction = null;
        this.nextInstruction = null;
    }

    private Instruction getnextInstruction() {

        if (this.isClosed())
            return null;

        Instruction inst = null;
        try {
            inst = this.channel.take();

            // convert poison to null
            if (inst == NullInstruction.NullInstance) {
                this.close();
                inst = null;
            }

        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return inst;
    }

    @Override
    public Instruction next() {

        // first call
        if (this.inited == false) {
            this.nextInstruction = this.getnextInstruction();
            this.inited = true;
        }

        if (this.nextInstruction == null) {
            return null;
        }

        this.currentInstruction = this.nextInstruction;
        this.nextInstruction = this.getnextInstruction();
        return this.currentInstruction;
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
    public void rawDump() {
        return;
    }

    @Override
    public boolean hasNext() {
        return this.nextInstruction != null;
    }

    @Override
    public void close() {
        this.isClosed = true;
    }

    @Override
    public boolean isClosed() {
        return this.isClosed;
    }
}
