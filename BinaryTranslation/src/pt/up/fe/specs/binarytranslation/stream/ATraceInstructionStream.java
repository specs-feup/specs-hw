package pt.up.fe.specs.binarytranslation.stream;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.producer.InstructionProducer;
import pt.up.fe.specs.binarytranslation.producer.detailed.RegisterDump;

public abstract class ATraceInstructionStream extends AInstructionStream implements TraceInstructionStream {

    /*
     * Output from GDB + QEMU Execution
     */
    protected ATraceInstructionStream(InstructionProducer traceProducer) {
        super(traceProducer);
    }

    @Override
    public boolean advanceTo(long addr) {
        return this.getProducer().advanceTo(addr);
    }

    @Override
    public Instruction nextInstruction() {

        // testing this
        var regs = this.getCurrentRegisters();

        var newinst = super.nextInstruction();
        if (this.numinsts % 10000 == 0 && !this.isSilent()) {
            System.out.println(this.numinsts + " instructions simulated...");
        }

        newinst.setRegisters(regs);
        return newinst;
    }

    @Override
    public RegisterDump getCurrentRegisters() {
        return this.getProducer().getRegisters();
    }
}
