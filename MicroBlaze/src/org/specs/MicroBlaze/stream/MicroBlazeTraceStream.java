package org.specs.MicroBlaze.stream;

import java.io.File;

import org.specs.MicroBlaze.asm.MicroBlazeApplication;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.producer.ChanneledInstructionProducer;
import pt.up.fe.specs.binarytranslation.producer.TraceInstructionProducer;
import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;
import pt.up.fe.specs.util.collections.concurrentchannel.ChannelConsumer;

public class MicroBlazeTraceStream extends ATraceInstructionStream {

    /*
     * Auxiliary constructor so that streams can be built from the threading engine
     * with the channels created by the parent InstructionProducer
     */
    public MicroBlazeTraceStream(File elfname, ChannelConsumer<Instruction> channel) {
        super(new ChanneledInstructionProducer(new MicroBlazeApplication(elfname), channel));
        // this.elfdump = MicroBlazeTraceStream.GDBbugHandle(elfname);
    }

    public MicroBlazeTraceStream(File elfname) {
        super(new MicroBlazeTraceProvider(elfname));
        // this.elfdump = MicroBlazeTraceStream.GDBbugHandle(elfname);
    }

    /**
     * Use a custom trace provider instead of the default
     * 
     * @param prod
     *            an initialized custom trace provider
     */
    public MicroBlazeTraceStream(TraceInstructionProducer prod) {
        this(prod.getApp().getElffile());
    }
}
