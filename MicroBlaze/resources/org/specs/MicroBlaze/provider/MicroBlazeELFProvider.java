package org.specs.MicroBlaze.provider;

import org.specs.MicroBlaze.asm.MicroBlazeApplication;

import pt.up.fe.specs.binarytranslation.ELFProvider;
import pt.up.fe.specs.binarytranslation.asm.Application;

public interface MicroBlazeELFProvider extends ELFProvider {

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
    default public String getResource() {
        return PREFIX + this.getClass().getSimpleName() + ".7z";
    }
}
