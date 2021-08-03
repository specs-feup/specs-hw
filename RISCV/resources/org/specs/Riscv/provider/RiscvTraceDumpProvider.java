package org.specs.Riscv.provider;

import pt.up.fe.specs.binarytranslation.ATraceDumpProvider;

public class RiscvTraceDumpProvider extends ATraceDumpProvider implements RiscvELFProvider {

    public RiscvTraceDumpProvider(RiscvELFProvider elfprovider) {
        super(elfprovider);
    }

    public RiscvELFProvider getOriginal() {
        return (RiscvELFProvider) this.original;
    }
}
