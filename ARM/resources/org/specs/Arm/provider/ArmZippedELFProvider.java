package org.specs.Arm.provider;

import pt.up.fe.specs.binarytranslation.ZippedELFProvider;

public interface ArmZippedELFProvider extends ArmELFProvider, ZippedELFProvider {

    @Override
    default ArmELFProvider asTraceTxtDump() {
        return new ArmObjDumpProvider(this);
    }

    @Override
    default ArmELFProvider asTxtDump() {
        return new ArmObjDumpProvider(this);
    }
}
