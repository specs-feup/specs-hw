package org.specs.MicroBlaze.stream;

import org.specs.MicroBlaze.asm.MicroBlazeApplication;
import org.specs.MicroBlaze.provider.MicroBlazeELFProvider;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.producer.ChanneledInstructionProducer;
import pt.up.fe.specs.binarytranslation.stream.AStaticInstructionStream;
import pt.up.fe.specs.util.collections.concurrentchannel.ChannelConsumer;

public class MicroBlazeElfStream extends AStaticInstructionStream {

    /*
     * Auxiliary constructor so that streams can be built from the threading engine
     * with the channels created by the parent InstructionProducer
     */
    public MicroBlazeElfStream(MicroBlazeApplication app, ChannelConsumer<Instruction> channel) {
        super(new ChanneledInstructionProducer(app, channel));
    }

    public MicroBlazeElfStream(MicroBlazeApplication app) {
        super(new MicroBlazeStaticProducer(app));
    }

    public MicroBlazeElfStream(MicroBlazeELFProvider elfprovider) {
        super(new MicroBlazeStaticProducer(elfprovider.toApplication()));
    }
}
