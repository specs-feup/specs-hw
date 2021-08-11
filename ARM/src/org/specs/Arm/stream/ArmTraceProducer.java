package org.specs.Arm.stream;

import org.specs.Arm.ArmApplication;
import org.specs.Arm.instruction.ArmInstruction;

import pt.up.fe.specs.binarytranslation.producer.TraceInstructionProducer;

public class ArmTraceProducer extends TraceInstructionProducer {

    public ArmTraceProducer(ArmApplication app) {
        super(app, ArmInstruction::newInstance);
    }
}
