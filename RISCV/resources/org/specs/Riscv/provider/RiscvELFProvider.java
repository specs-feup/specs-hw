package org.specs.Riscv.provider;

import org.specs.Riscv.RiscvApplication;

import pt.up.fe.specs.binarytranslation.ELFProvider;
import pt.up.fe.specs.binarytranslation.asm.Application;

public interface RiscvELFProvider extends ELFProvider {

    final static String PREFIX = "org/specs/Riscv/asm/";

    @Override
    default RiscvELFProvider asTraceTxtDump() {
        return new RiscvTraceDumpProvider(this);
    }

    @Override
    default RiscvELFProvider asTxtDump() {
        return new RiscvObjDumpProvider(this);
    }

    @Override
    default Application toApplication() {
        return new RiscvApplication(this);
    }

    @Override
    default public String getResource() {
        return PREFIX + this.getClass().getSimpleName() + ".7z";
    }
}
