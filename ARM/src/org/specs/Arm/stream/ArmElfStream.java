package org.specs.Arm.stream;

import org.specs.Arm.ArmApplication;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.producer.ChanneledInstructionProducer;
import pt.up.fe.specs.binarytranslation.stream.AStaticInstructionStream;
import pt.up.fe.specs.util.collections.concurrentchannel.ChannelConsumer;

public class ArmElfStream extends AStaticInstructionStream {

    /*
     * Auxiliary constructor so that streams can be built from the threading engine
     * with the channels created by the parent InstructionProducer
     */
    public ArmElfStream(ArmApplication app, ChannelConsumer<Instruction> channel) {
        super(new ChanneledInstructionProducer(app, channel));
    }

    public ArmElfStream(ArmApplication app) {
        super(new ArmStaticProducer(app));
    }
}
