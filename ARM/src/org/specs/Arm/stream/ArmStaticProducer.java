package org.specs.Arm.stream;

import org.specs.Arm.ArmApplication;
import org.specs.Arm.ArmELFProvider;
import org.specs.Arm.ArmResource;
import org.specs.Arm.instruction.ArmInstruction;

import pt.up.fe.specs.binarytranslation.producer.StaticInstructionProducer;

public class ArmStaticProducer extends StaticInstructionProducer {

    public ArmStaticProducer(ArmELFProvider elfprovider) {
        super(new ArmApplication(elfprovider),
                ArmResource.AARCH64_DUMP_REGEX,
                ArmInstruction::newInstance);
    }
}
