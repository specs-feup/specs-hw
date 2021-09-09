package org.specs.MicroBlaze.stream;

import org.specs.MicroBlaze.asm.MicroBlazeApplication;
import org.specs.MicroBlaze.provider.MicroBlazeELFProvider;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.producer.ChanneledInstructionProducer;
import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;
import pt.up.fe.specs.util.collections.concurrentchannel.ChannelConsumer;

public class MicroBlazeTraceStream extends ATraceInstructionStream {

    /*
     * Auxiliary constructor so that streams can be built from the threading engine
     * with the channels created by the parent InstructionProducer
     */
    public MicroBlazeTraceStream(MicroBlazeApplication app, ChannelConsumer<Instruction> channel) {
        super(new ChanneledInstructionProducer(app, channel));
    }

    public MicroBlazeTraceStream(MicroBlazeApplication app) {
        super(new MicroBlazeTraceProducer(app));
    }

    public MicroBlazeTraceStream(MicroBlazeELFProvider elfprovider) {
        super(new MicroBlazeTraceProducer(elfprovider.toApplication()));
    }
}
