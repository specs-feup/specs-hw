package pt.up.fe.specs.binarytranslation.stream;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.v2.ATraceInstructionProducer;

public abstract class ATraceInstructionStream extends AInstructionStream {

    /*
     * 
     */
    protected ATraceInstructionStream(ATraceInstructionProducer traceProducer) {
        super(traceProducer);
    }

    @Override
    public Instruction nextInstruction() {

        var newinst = super.nextInstruction();
        if (this.numinsts % 1000 == 0) {
            System.out.println(this.numinsts + " instructions simulated...");
        }

        return newinst;
    }
}
