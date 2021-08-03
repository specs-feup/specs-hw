package org.specs.MicroBlaze.provider;

import org.specs.MicroBlaze.MicroBlazeResource;
import org.specs.MicroBlaze.asm.MicroBlazeApplication;

import pt.up.fe.specs.binarytranslation.ELFProvider;
import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.util.providers.ResourceProvider;

public interface MicroBlazeELFProvider extends ELFProvider {

    @Override
    default public ResourceProvider getCPUName() {
        return MicroBlazeResource.MICROBLAZE_CPU_NAME;
    }

    @Override
    default Application toApplication() {
        return new MicroBlazeApplication(this);
    }
}
