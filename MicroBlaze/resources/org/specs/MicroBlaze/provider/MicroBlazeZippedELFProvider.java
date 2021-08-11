package org.specs.MicroBlaze.provider;

import pt.up.fe.specs.binarytranslation.ZippedELFProvider;

public interface MicroBlazeZippedELFProvider extends MicroBlazeELFProvider, ZippedELFProvider {

    @Override
    default MicroBlazeELFProvider asTraceTxtDump() {
        return new MicroBlazeTraceDumpProvider(this);
    }

    @Override
    default MicroBlazeELFProvider asTxtDump() {
        return new MicroBlazeObjDumpProvider(this);
    }
}
