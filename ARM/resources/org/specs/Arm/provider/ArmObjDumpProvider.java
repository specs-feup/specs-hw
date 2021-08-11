package org.specs.Arm.provider;

import pt.up.fe.specs.binarytranslation.ObjDumpProvider;

public class ArmObjDumpProvider extends ObjDumpProvider implements ArmELFProvider {

    public ArmObjDumpProvider(ArmELFProvider elfprovider) {
        super(elfprovider);
    }
}
