package org.specs.MicroBlaze.provider;

import org.specs.MicroBlaze.asm.MicroBlazeApplication;
import org.specs.MicroBlaze.stream.MicroBlazeElfStream;
import org.specs.MicroBlaze.stream.MicroBlazeTraceStream;

import pt.up.fe.specs.binarytranslation.ELFProvider;
import pt.up.fe.specs.binarytranslation.stream.StaticInstructionStream;
import pt.up.fe.specs.binarytranslation.stream.TraceInstructionStream;

public interface MicroBlazeELFProvider extends ELFProvider {

    final static String PREFIX = "org/specs/MicroBlaze/asm/";

    @Override
    default MicroBlazeApplication toApplication() {
        return new MicroBlazeApplication(this);
    }

    @Override
    default StaticInstructionStream toStaticStream() {
        return new MicroBlazeElfStream(this.toApplication());
    }

    @Override
    default TraceInstructionStream toTraceStream() {
        return new MicroBlazeTraceStream(this.toApplication());
    }

    @Override
    default public String getResource() {
        return PREFIX + this.getClass().getSimpleName() + ".7z";
    }
}
