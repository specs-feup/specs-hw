package org.specs.MicroBlaze.provider;

import pt.up.fe.specs.binarytranslation.ObjDumpProvider;

public class MicroBlazeObjDumpProvider extends ObjDumpProvider implements MicroBlazeELFProvider {

    public MicroBlazeObjDumpProvider(MicroBlazeELFProvider elfprovider) {
        super(elfprovider);
    }
}
