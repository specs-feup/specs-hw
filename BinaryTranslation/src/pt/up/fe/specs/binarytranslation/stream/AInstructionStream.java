package pt.up.fe.specs.binarytranslation.stream;

import com.google.gson.annotations.Expose;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.v2.InstructionProducer;

public abstract class AInstructionStream implements InstructionStream {

    @Expose
    protected long numinsts;

    @Expose
    protected long numcycles;

    /*
     * This type of stream contains its own producer internally
     */
    private InstructionProducer producer;

    public AInstructionStream(InstructionProducer producer) {
        this.producer = producer;
        this.numinsts = 0;
        this.numcycles = 0;
    }

    @Override
    public void rawDump() {
        this.producer.rawDump();
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
    public Instruction nextInstruction() {
        return this.producer.nextInstruction();
    }

    @Override
    public void close() {
        try {
            this.producer.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
