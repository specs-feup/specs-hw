package org.specs.Riscv.stream.detailed;

import java.io.File;

import org.specs.Riscv.RiscvApplication;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.producer.ChanneledInstructionProducer;
import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;
import pt.up.fe.specs.util.collections.concurrentchannel.ChannelConsumer;

public class RiscvDetailedTraceStream extends ATraceInstructionStream {

    public RiscvDetailedTraceStream(File elfname, ChannelConsumer<Instruction> channel) {
        super(new ChanneledInstructionProducer(new RiscvApplication(elfname), channel));
    }
    
    public RiscvDetailedTraceStream(File elfname) {
        super(new RiscvDetailedTraceProvider(elfname));
    }
}
