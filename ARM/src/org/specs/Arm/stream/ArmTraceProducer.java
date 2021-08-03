package org.specs.Arm.stream;

import org.specs.Arm.ArmApplication;
import org.specs.Arm.ArmELFProvider;
import org.specs.Arm.ArmResource;
import org.specs.Arm.instruction.ArmInstruction;

import pt.up.fe.specs.binarytranslation.producer.TraceInstructionProducer;

public class ArmTraceProducer extends TraceInstructionProducer {

    public ArmTraceProducer(ArmELFProvider elfprovide) {
        super(new ArmApplication(elfprovide),
                ArmResource.AARCH64_TRACE_REGEX,
                ArmInstruction::newInstance);
    }
}
