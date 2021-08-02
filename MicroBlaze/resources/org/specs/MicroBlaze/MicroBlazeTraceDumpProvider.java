package org.specs.MicroBlaze;

import pt.up.fe.specs.binarytranslation.ATraceDumpProvider;

public class MicroBlazeTraceDumpProvider extends ATraceDumpProvider implements MicroBlazeELFProvider {

    public MicroBlazeTraceDumpProvider(MicroBlazeELFProvider elfprovider) {
        super(elfprovider);
    }

    public MicroBlazeELFProvider getOriginal() {
        return (MicroBlazeELFProvider) this.original;
    }
}