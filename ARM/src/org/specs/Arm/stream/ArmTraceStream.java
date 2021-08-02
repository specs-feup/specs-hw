package org.specs.Arm.stream;

import org.specs.Arm.ArmApplication;
import org.specs.Arm.ArmELFProvider;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.producer.ChanneledInstructionProducer;
import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;
import pt.up.fe.specs.util.collections.concurrentchannel.ChannelConsumer;

public class ArmTraceStream extends ATraceInstructionStream {

    /*
     * Auxiliary constructor so that streams can be built from the threading engine
     * with the channels created by the parent InstructionProducer
     */
    public ArmTraceStream(ArmELFProvider elfprovider, ChannelConsumer<Instruction> channel) {
        super(new ChanneledInstructionProducer(new ArmApplication(elfprovider), channel));
    }

    public ArmTraceStream(ArmELFProvider elfprovider) {
        super(new ArmTraceProducer(elfprovider));
    }
}
