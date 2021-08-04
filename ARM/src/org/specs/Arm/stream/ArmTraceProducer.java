package org.specs.Arm.stream;

import org.specs.Arm.ArmApplication;
import org.specs.Arm.instruction.ArmInstruction;
import org.specs.Arm.provider.ArmELFProvider;

import pt.up.fe.specs.binarytranslation.producer.TraceInstructionProducer;

public class ArmTraceProducer extends TraceInstructionProducer {

    public ArmTraceProducer(ArmELFProvider elfprovide) {
        super(new ArmApplication(elfprovide),
                ArmInstruction::newInstance);
    }
}
