package org.specs.MicroBlaze.provider;

import org.specs.MicroBlaze.asm.MicroBlazeApplication;
import org.specs.MicroBlaze.stream.MicroBlazeElfStream;
import org.specs.MicroBlaze.stream.MicroBlazeTraceStream;

import pt.up.fe.specs.binarytranslation.ZippedELFProvider;
import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.binarytranslation.stream.StaticInstructioStream;
import pt.up.fe.specs.binarytranslation.stream.TraceInstructionStream;

public interface MicroBlazeELFProvider extends ZippedELFProvider {

    final static String PREFIX = "org/specs/MicroBlaze/asm/";

    @Override
    default MicroBlazeELFProvider asTraceTxtDump() {
        return new MicroBlazeTraceDumpProvider(this);
    }

    @Override
    default MicroBlazeELFProvider asTxtDump() {
        return new MicroBlazeObjDumpProvider(this);
    }

    @Override
    default Application toApplication() {
        return new MicroBlazeApplication(this);
    }

    @Override
    default StaticInstructioStream toStaticStream() {
        return new MicroBlazeElfStream(this);
    }

    @Override
    default TraceInstructionStream toTraceStream() {
        return new MicroBlazeTraceStream(this);
    }

    @Override
    default public String getResource() {
        return PREFIX + this.getClass().getSimpleName() + ".7z";
    }
}
