package org.specs.Riscv;

import pt.up.fe.specs.binarytranslation.ELFProvider;
import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.util.providers.ResourceProvider;

public interface RiscvELFProvider extends ELFProvider {

    @Override
    default public ResourceProvider getCPUName() {
        return RiscvResource.RISCV_CPU_NAME;
    }

    @Override
    default Application toApplication() {
        return new RiscvApplication(this);
    }
}
