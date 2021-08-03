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
    public MicroBlazeElfStream(MicroBlazeELFProvider provider, ChannelConsumer<Instruction> channel) {
        super(new ChanneledInstructionProducer(new MicroBlazeApplication(provider), channel));
    }

    public MicroBlazeElfStream(MicroBlazeELFProvider provider) {
        super(new MicroBlazeStaticProducer(provider));
    }

    /*
     * MicroBlazeELFDump  --> child of StaticInstructionproducer ??
     * 
     * NO: (MicroBlazeELFDump already exists as another object!) - its a in memory ObjDump (whic is a process run of objdump)
     * 
     *     public MicroBlazeElfStream(MicroBlazeELFDump txtelfDump) {
        super(txtelfDump);
    }
     * 
     */
}
