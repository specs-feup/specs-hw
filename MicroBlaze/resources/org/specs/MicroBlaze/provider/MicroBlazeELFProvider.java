package org.specs.MicroBlaze.provider;

import org.specs.MicroBlaze.MicroBlazeResource;
import org.specs.MicroBlaze.asm.MicroBlazeApplication;

import pt.up.fe.specs.binarytranslation.ELFProvider;
import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.util.providers.ResourceProvider;

public interface MicroBlazeELFProvider extends ELFProvider {

    final static String PREFIX = "org/specs/MicroBlaze/asm/";

    @Override
    default public ResourceProvider getCPUName() {
        return MicroBlazeResource.MICROBLAZE_CPU_NAME;
    }

    @Override
    default Application toApplication() {
        return new MicroBlazeApplication(this);
    }

    @Override
    default public String getResource() {
        return this.getClass().getSimpleName() + ".7z";
    }
}
