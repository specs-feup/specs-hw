package org.specs.Arm.stream;

import java.io.File;

import org.specs.Arm.ArmApplication;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.producer.ChanneledInstructionProducer;
import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;
import pt.up.fe.specs.util.collections.concurrentchannel.ChannelConsumer;

public class ArmTraceStream extends ATraceInstructionStream {

    /*
     * Auxiliary constructor so that streams can be built from the threading engine
     * with the channels created by the parent InstructionProducer
     */
    public ArmTraceStream(File elfname, ChannelConsumer<Instruction> channel) {
        super(new ChanneledInstructionProducer(new ArmApplication(elfname), channel));
    }

    public ArmTraceStream(File elfname) {
        super(new ArmTraceProvider(elfname));
    }
}
