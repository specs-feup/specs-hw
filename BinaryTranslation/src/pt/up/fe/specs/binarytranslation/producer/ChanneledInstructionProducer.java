package pt.up.fe.specs.binarytranslation.producer;

import com.google.gson.annotations.Expose;

import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.NullInstruction;
import pt.up.fe.specs.util.collections.concurrentchannel.ChannelConsumer;
import pt.up.fe.specs.util.threadstream.GenericObjectStream;

public class ChanneledInstructionProducer extends GenericObjectStream<Instruction> implements InstructionProducer {

    /*
     * Interesting as output, and should be queryable by over-arching BTF chain
     */
    @Expose
    private final Application app;

    public ChanneledInstructionProducer(Application app, ChannelConsumer<Instruction> consumer) {
        super(consumer, NullInstruction.NullInstance);
        this.app = app;
    }

    @Override
    public Integer getInstructionWidth() {
        return this.app.getInstructionWidth();
    }

    @Override
    public Instruction nextInstruction() {
        return this.next();
    }

    @Override
    public void rawDump() {
        // TODO Auto-generated method stub
    }

    @Override
    public Application getApp() {
        return this.app;
    }
}
