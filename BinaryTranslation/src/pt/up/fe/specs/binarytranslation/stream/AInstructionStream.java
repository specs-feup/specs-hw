package pt.up.fe.specs.binarytranslation.stream;

import com.google.gson.annotations.Expose;

import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.producer.InstructionProducer;
import pt.up.fe.specs.util.threadstream.AObjectStream;

public abstract class AInstructionStream extends AObjectStream<Instruction> implements InstructionStream {

    @Expose
    protected long numinsts;

    @Expose
    protected long numcycles;

    /*
     * This type of stream contains its own producer internally
     */
    private InstructionProducer producer;

    /*
     * 
     */
    private boolean silent = true;

    public AInstructionStream(InstructionProducer producer) {
        super(producer.getPoison());
        this.producer = producer;
        this.numinsts = 0;
        this.numcycles = 0;
    }

    @Override
    public void silent(boolean isSilent) {
        this.silent = isSilent;
    }

    protected boolean isSilent() {
        return silent;
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
    protected Instruction consumeFromProvider() {
        return this.producer.nextInstruction();
    }

    @Override
    public Instruction nextInstruction() {
        var inst = this.next();
        if (inst == null)
            return null;

        this.numcycles += inst.getLatency();
        this.numinsts++;

        // if (this.dumpStream)
        // System.out.println(inst.getRepresentation());

        return inst;
    }

    @Override
    public final Application getApp() {
        return this.producer.getApp();
    }

    @Override
    public final Integer getInstructionWidth() {
        return this.getApp().getInstructionWidth();
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
