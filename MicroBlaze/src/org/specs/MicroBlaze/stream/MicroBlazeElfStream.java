package org.specs.MicroBlaze.stream;

import java.io.File;

import org.specs.MicroBlaze.MicroBlazeApplication;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.producer.ChanneledInstructionProducer;
import pt.up.fe.specs.binarytranslation.stream.AStaticInstructionStream;
import pt.up.fe.specs.util.collections.concurrentchannel.ChannelConsumer;

public class MicroBlazeElfStream extends AStaticInstructionStream {

    /*
     * Auxiliary constructor so that streams can be built from the threading engine
     * with the channels created by the parent InstructionProducer
     */
    public MicroBlazeElfStream(File elfname, ChannelConsumer<Instruction> channel) {
        super(new ChanneledInstructionProducer(new MicroBlazeApplication(elfname), channel));
    }

    public MicroBlazeElfStream(File elfname) {
        super(new MicroBlazeDump(elfname));
    }

    @Override
    public int getInstructionWidth() {
        return 4; // return in bytes
    }
}
