package org.specs.Riscv.stream;

import org.specs.Riscv.RiscvApplication;
import org.specs.Riscv.RiscvELFProvider;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.producer.ChanneledInstructionProducer;
import pt.up.fe.specs.binarytranslation.stream.AStaticInstructionStream;
import pt.up.fe.specs.util.collections.concurrentchannel.ChannelConsumer;

public class RiscvElfStream extends AStaticInstructionStream {

    /*
     * Auxiliary constructor so that streams can be built from the threading engine
     * with the channels created by the parent InstructionProducer
     */
    public RiscvElfStream(RiscvELFProvider elfprovider, ChannelConsumer<Instruction> channel) {
        super(new ChanneledInstructionProducer(new RiscvApplication(elfprovider), channel));
    }

    public RiscvElfStream(RiscvELFProvider elfprovider) {
        super(new RiscvStaticProducer(elfprovider));
    }
}
