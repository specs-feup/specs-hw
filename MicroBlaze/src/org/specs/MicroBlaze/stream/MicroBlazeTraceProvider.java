package org.specs.MicroBlaze.stream;

import java.io.File;

import org.specs.MicroBlaze.MicroBlazeResource;
import org.specs.MicroBlaze.asm.MicroBlazeApplication;
import org.specs.MicroBlaze.instruction.MicroBlazeInstruction;

import pt.up.fe.specs.binarytranslation.producer.TraceInstructionProducer;

public class MicroBlazeTraceProvider extends TraceInstructionProducer {

    public MicroBlazeTraceProvider(File elfname) {
        super(new MicroBlazeApplication(elfname),
                MicroBlazeResource.MICROBLAZE_INSTRUCTION_TRACE_REGEX,
                MicroBlazeInstruction::newInstance);
    }
}
