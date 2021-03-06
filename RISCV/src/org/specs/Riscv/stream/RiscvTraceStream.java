package org.specs.Riscv.stream;

import java.io.File;

import org.specs.Riscv.RiscvApplication;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.producer.ChanneledInstructionProducer;
import pt.up.fe.specs.binarytranslation.producer.TraceInstructionProducer;
import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;
import pt.up.fe.specs.util.collections.concurrentchannel.ChannelConsumer;

public class RiscvTraceStream extends ATraceInstructionStream {

    /*
     * Auxiliary constructor so that streams can be built from the threading engine
     * with the channels created by the parent InstructionProducer
     */
    public RiscvTraceStream(File elfname, ChannelConsumer<Instruction> channel) {
        super(new ChanneledInstructionProducer(new RiscvApplication(elfname), channel));
    }

    public RiscvTraceStream(File elfname) {
        super(new RiscvTraceProvider(elfname));
    }

    /**
     * Use a custom trace provider instead of the default
     * @param prod an initialized custom trace provider
     */
    public RiscvTraceStream(TraceInstructionProducer prod) {
        super(prod);
    }
}
