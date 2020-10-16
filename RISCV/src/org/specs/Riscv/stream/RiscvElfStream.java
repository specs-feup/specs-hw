package org.specs.Riscv.stream;

import java.io.File;

import org.specs.Riscv.RiscvApplication;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.producer.ChanneledInstructionProducer;
import pt.up.fe.specs.binarytranslation.stream.AStaticInstructionStream;
import pt.up.fe.specs.util.collections.concurrentchannel.ChannelConsumer;

public class RiscvElfStream extends AStaticInstructionStream {

    /*
     * Auxiliary constructor so that streams can be built from the threading engine
     * with the channels created by the parent InstructionProducer
     */
    public RiscvElfStream(File elfname, ChannelConsumer<Instruction> channel) {
        super(new ChanneledInstructionProducer(new RiscvApplication(elfname), channel));
    }

    public RiscvElfStream(File elfname) {
        super(new RiscvDump(elfname));
    }

    @Override
    public int getInstructionWidth() {
        return 4; // return in bytes
        // TODO replace this with something smarter
    }
}
