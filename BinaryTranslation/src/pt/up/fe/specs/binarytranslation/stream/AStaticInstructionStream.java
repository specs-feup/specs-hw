package pt.up.fe.specs.binarytranslation.stream;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.producer.InstructionProducer;

public abstract class AStaticInstructionStream extends AInstructionStream {

    /*
     * Output from GNU based objdump
     */
    protected AStaticInstructionStream(InstructionProducer staticProducer) {
        super(staticProducer);
    }

    @Override
    public InstructionStreamType getType() {
        return InstructionStreamType.STATIC_ELF;
    }

    @Override
    public Instruction nextInstruction() {

        var newinst = super.nextInstruction();
        if (this.numinsts % 10000 == 0 && !this.isSilent()) {
            System.out.println(this.numinsts + " instructions processed...");
        }

        return newinst;
    }
}
