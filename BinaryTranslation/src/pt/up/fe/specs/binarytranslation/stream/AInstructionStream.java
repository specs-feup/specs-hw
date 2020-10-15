package pt.up.fe.specs.binarytranslation.stream;

import com.google.gson.annotations.Expose;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.replicator.AObjectStream;
import pt.up.fe.specs.binarytranslation.stream.v2.InstructionProducer;

public abstract class AInstructionStream extends AObjectStream<Instruction> implements InstructionStream {

    @Expose
    protected long numinsts;

    @Expose
    protected long numcycles;

    /*
     * This type of stream contains its own producer internally
     */
    private InstructionProducer producer;

    public AInstructionStream(InstructionProducer producer) {
        super(producer.getPoison());
        this.producer = producer;
        this.numinsts = 0;
        this.numcycles = 0;
    }

    @Override
    public long getNumInstructions() {
        return this.numinsts;
    }

    @Override
    public long getCycles() {
        return this.numcycles;
    }

    @Override
    protected Instruction consumeFromProvider() {
        return this.producer.nextInstruction();
    }

    @Override
    public Instruction nextInstruction() {
        var inst = this.next();
        this.numcycles += inst.getLatency();
        this.numinsts++;
        return inst;
    }

    @Override
    public void close() {
        try {
            this.producer.close();
            super.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
