package org.specs.Arm.stream;

import org.specs.Arm.ArmApplication;
import org.specs.Arm.provider.ArmELFProvider;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.producer.ChanneledInstructionProducer;
import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;
import pt.up.fe.specs.util.collections.concurrentchannel.ChannelConsumer;

public class ArmTraceStream extends ATraceInstructionStream {

    /*
     * Auxiliary constructor so that streams can be built from the threading engine
     * with the channels created by the parent InstructionProducer
     */
    public ArmTraceStream(ArmApplication app, ChannelConsumer<Instruction> channel) {
        super(new ChanneledInstructionProducer(app, channel));
    }

    public ArmTraceStream(ArmApplication app) {
        super(new ArmTraceProducer(app));
    }

    public ArmTraceStream(ArmELFProvider elfprovider) {
        super(new ArmTraceProducer(elfprovider.toApplication()));
    }
}
