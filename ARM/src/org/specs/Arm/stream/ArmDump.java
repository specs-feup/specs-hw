package org.specs.Arm.stream;

import java.io.File;

import org.specs.Arm.ArmApplication;
import org.specs.Arm.ArmResource;
import org.specs.Arm.instruction.ArmInstruction;

import pt.up.fe.specs.binarytranslation.producer.StaticInstructionProducer;

public class ArmDump extends StaticInstructionProducer {

    public ArmDump(File elfname) {
        super(new ArmApplication(elfname),
                ArmResource.AARCH64_REGEX,
                ArmInstruction::newInstance);
    }
}
