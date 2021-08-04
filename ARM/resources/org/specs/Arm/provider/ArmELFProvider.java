package org.specs.Arm.provider;

import org.specs.Arm.ArmApplication;

import pt.up.fe.specs.binarytranslation.ELFProvider;
import pt.up.fe.specs.binarytranslation.asm.Application;

public interface ArmELFProvider extends ELFProvider {

    final static String PREFIX = "org/specs/Arm/asm/";

    @Override
    default Application toApplication() {
        return new ArmApplication(this);
    }

    @Override
    default public String getResource() {
        return PREFIX + this.getClass().getSimpleName() + ".7z";
    }
}
