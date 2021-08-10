package org.specs.Arm.provider;

import org.specs.Arm.ArmApplication;

import pt.up.fe.specs.binarytranslation.ZippedELFProvider;
import pt.up.fe.specs.binarytranslation.asm.Application;

public interface ArmELFProvider extends ZippedELFProvider {

    final static String PREFIX = "org/specs/Arm/asm/";

    @Override
    default ArmELFProvider asTraceTxtDump() {
        return new ArmObjDumpProvider(this);
    }

    @Override
    default ArmELFProvider asTxtDump() {
        return new ArmObjDumpProvider(this);
    }

    @Override
    default Application toApplication() {
        return new ArmApplication(this);
    }

    @Override
    default public String getResource() {
        return PREFIX + this.getClass().getSimpleName() + ".7z";
    }
}
