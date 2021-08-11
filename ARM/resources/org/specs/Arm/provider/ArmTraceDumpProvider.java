package org.specs.Arm.provider;

import pt.up.fe.specs.binarytranslation.TraceDumpProvider;

public class ArmTraceDumpProvider extends TraceDumpProvider implements ArmELFProvider {

    public ArmTraceDumpProvider(ArmELFProvider elfprovider) {
        super(elfprovider);
    }
}
