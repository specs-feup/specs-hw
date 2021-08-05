package org.specs.Arm.stream;

import org.specs.Arm.ArmApplication;
import org.specs.Arm.instruction.ArmInstruction;
import org.specs.Arm.provider.ArmELFProvider;

import pt.up.fe.specs.binarytranslation.producer.StaticInstructionProducer;

public class ArmStaticProducer extends StaticInstructionProducer {

    public ArmStaticProducer(ArmELFProvider elfprovider) {
        super(new ArmApplication(elfprovider),
                ArmInstruction::newInstance);
    }
}
