package org.specs.Riscv.provider;

import pt.up.fe.specs.binarytranslation.TraceDumpProvider;

public class RiscvTraceDumpProvider extends TraceDumpProvider implements RiscvELFProvider {

    public RiscvTraceDumpProvider(RiscvELFProvider elfprovider) {
        super(elfprovider);
    }
}
