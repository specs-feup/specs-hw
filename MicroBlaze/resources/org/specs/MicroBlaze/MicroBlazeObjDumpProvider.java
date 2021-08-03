package org.specs.MicroBlaze;

import pt.up.fe.specs.binarytranslation.AObjDumpProvider;

public class MicroBlazeObjDumpProvider extends AObjDumpProvider implements MicroBlazeELFProvider {

    public MicroBlazeObjDumpProvider(MicroBlazeELFProvider elfprovider) {
        super(elfprovider);
    }
}
