package org.specs.Arm.stream;

import org.specs.Arm.ArmApplication;
import org.specs.Arm.instruction.ArmInstruction;

import pt.up.fe.specs.binarytranslation.producer.StaticInstructionProducer;

public class ArmStaticProducer extends StaticInstructionProducer {

    public ArmStaticProducer(ArmApplication app) {
        super(app, ArmInstruction::newInstance);
    }
}
