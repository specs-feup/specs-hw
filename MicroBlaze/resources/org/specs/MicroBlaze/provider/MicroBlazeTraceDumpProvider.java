package org.specs.MicroBlaze.provider;

import pt.up.fe.specs.binarytranslation.TraceDumpProvider;

public class MicroBlazeTraceDumpProvider extends TraceDumpProvider implements MicroBlazeELFProvider {

    public MicroBlazeTraceDumpProvider(MicroBlazeELFProvider elfprovider) {
        super(elfprovider);
    }
}