package org.specs.Arm.stream;

import org.specs.Arm.ArmApplication;
import org.specs.Arm.provider.ArmELFProvider;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.producer.ChanneledInstructionProducer;
import pt.up.fe.specs.binarytranslation.stream.AStaticInstructionStream;
import pt.up.fe.specs.util.collections.concurrentchannel.ChannelConsumer;

public class ArmElfStream extends AStaticInstructionStream {

    /*
     * Auxiliary constructor so that streams can be built from the threading engine
     * with the channels created by the parent InstructionProducer
     */
    public ArmElfStream(ArmELFProvider elfprovider, ChannelConsumer<Instruction> channel) {
        super(new ChanneledInstructionProducer(new ArmApplication(elfprovider), channel));
    }

    public ArmElfStream(ArmELFProvider elfprovider) {
        super(new ArmStaticProducer(elfprovider));
    }
}
