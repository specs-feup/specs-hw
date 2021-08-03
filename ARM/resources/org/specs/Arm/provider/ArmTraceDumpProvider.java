package org.specs.Arm.provider;

import pt.up.fe.specs.binarytranslation.ATraceDumpProvider;

public class ArmTraceDumpProvider extends ATraceDumpProvider implements ArmELFProvider {

    public ArmTraceDumpProvider(ArmELFProvider elfprovider) {
        super(elfprovider);
    }
}
