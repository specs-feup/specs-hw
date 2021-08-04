package org.specs.MicroBlaze.stream;

import org.specs.MicroBlaze.asm.MicroBlazeApplication;
import org.specs.MicroBlaze.instruction.MicroBlazeInstruction;
import org.specs.MicroBlaze.provider.MicroBlazeELFProvider;

import pt.up.fe.specs.binarytranslation.producer.TraceInstructionProducer;

public class MicroBlazeTraceProducer extends TraceInstructionProducer {

    public MicroBlazeTraceProducer(MicroBlazeELFProvider elfprovider) {
        super(new MicroBlazeApplication(elfprovider),
                MicroBlazeInstruction::newInstance);
    }
}
