package org.specs.MicroBlaze.stream;

import java.io.File;

import org.specs.MicroBlaze.MicroBlazeApplication;
import org.specs.MicroBlaze.MicroBlazeResource;
import org.specs.MicroBlaze.instruction.MicroBlazeInstruction;

import pt.up.fe.specs.binarytranslation.producer.StaticInstructionProducer;

public class MicroBlazeDump extends StaticInstructionProducer {

    public MicroBlazeDump(File elfname) {
        super(new MicroBlazeApplication(elfname),
                MicroBlazeResource.MICROBLAZE_INSTRUCTION_REGEX,
                MicroBlazeInstruction::newInstance);
    }
}
