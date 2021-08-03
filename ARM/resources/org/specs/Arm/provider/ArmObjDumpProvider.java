package org.specs.Arm.provider;

import pt.up.fe.specs.binarytranslation.AObjDumpProvider;

public class ArmObjDumpProvider extends AObjDumpProvider implements ArmELFProvider {

    public ArmObjDumpProvider(ArmELFProvider elfprovider) {
        super(elfprovider);
    }
}
