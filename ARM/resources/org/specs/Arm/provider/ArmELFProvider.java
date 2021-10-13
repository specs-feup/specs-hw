package org.specs.Arm.provider;

import org.specs.Arm.ArmApplication;
import org.specs.Arm.stream.ArmElfStream;
import org.specs.Arm.stream.ArmTraceStream;

import pt.up.fe.specs.binarytranslation.ZippedELFProvider;
import pt.up.fe.specs.binarytranslation.stream.StaticInstructionStream;
import pt.up.fe.specs.binarytranslation.stream.TraceInstructionStream;

public interface ArmELFProvider extends ZippedELFProvider {

    final static String PREFIX = "org/specs/Arm/asm/";

    @Override
    default ArmApplication toApplication() {
        return new ArmApplication(this);
    }

    @Override
    default StaticInstructionStream toStaticStream() {
        return new ArmElfStream(this.toApplication());
    }

    @Override
    default TraceInstructionStream toTraceStream() {
        return new ArmTraceStream(this.toApplication());
    }

    @Override
    default public String getResource() {
        return PREFIX + this.getClass().getSimpleName() + ".7z";
    }
}
