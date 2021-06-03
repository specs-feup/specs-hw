package pt.up.fe.specs.binarytranslation.stream;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.producer.InstructionProducer;

public abstract class ATraceInstructionStream extends AInstructionStream {

    /*
     * Output from QEMU Execution
     */
    protected ATraceInstructionStream(InstructionProducer traceProducer) {
        super(traceProducer);
    }

    @Override
    public InstructionStreamType getType() {
        return InstructionStreamType.TRACE;
    }

    @Override
    public boolean advanceTo(long addr) {
        return this.getProducer().advanceTo(addr);
    }

    @Override
    public void setCycleCounterBounds(Number startAddr, Number stopAddr) {
        this.boundStartAddr = startAddr.longValue();
        this.boundStopAddr = stopAddr.longValue();
    }

    @Override
    public Instruction nextInstruction() {

        var newinst = super.nextInstruction();
        if (this.numBoundCycles % 10000 == 0 && !this.isSilent()) {
            System.out.println(this.numBoundCycles + " bound cycles simulated... at addr 0x"
                    + Long.toHexString(newinst.getAddress()));
        }

        return newinst;
    }
}
