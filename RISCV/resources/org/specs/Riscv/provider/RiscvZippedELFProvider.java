package org.specs.Riscv.provider;

import pt.up.fe.specs.binarytranslation.ZippedELFProvider;

public interface RiscvZippedELFProvider extends RiscvELFProvider, ZippedELFProvider {

    @Override
    default RiscvELFProvider asTraceTxtDump() {
        return new RiscvTraceDumpProvider(this);
    }

    @Override
    default RiscvELFProvider asTxtDump() {
        return new RiscvObjDumpProvider(this);
    }
}
