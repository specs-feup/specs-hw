package org.specs.Arm.provider;

import org.specs.Arm.ArmApplication;
import org.specs.Arm.ArmResource;

import pt.up.fe.specs.binarytranslation.ELFProvider;
import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.util.providers.ResourceProvider;

public interface ArmELFProvider extends ELFProvider {

    @Override
    default public ResourceProvider getCPUName() {
        return ArmResource.ARMv8_CPU_NAME;
    }

    @Override
    default Application toApplication() {
        return new ArmApplication(this);
    }
}
