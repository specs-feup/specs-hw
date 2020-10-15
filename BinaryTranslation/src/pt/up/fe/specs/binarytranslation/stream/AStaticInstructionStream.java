package pt.up.fe.specs.binarytranslation.stream;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.v2.AStaticInstructionProducer;

public abstract class AStaticInstructionStream extends AInstructionStream {

    /*
     * Output from GNU based objdump
     */
    protected AStaticInstructionStream(AStaticInstructionProducer staticProducer) {
        super(staticProducer);
    }

    @Override
    public InstructionStreamType getType() {
        return InstructionStreamType.STATIC_ELF;
    }

    @Override
    public Instruction nextInstruction() {

        var newinst = super.nextInstruction();
        if (this.numinsts % 1000 == 0) {
            System.out.println(this.numinsts + " instructions processed...");
        }

        return newinst;
    }
}
