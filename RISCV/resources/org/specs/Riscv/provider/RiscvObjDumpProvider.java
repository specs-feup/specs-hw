package org.specs.Riscv.provider;

import pt.up.fe.specs.binarytranslation.AObjDumpProvider;

public class RiscvObjDumpProvider extends AObjDumpProvider implements RiscvELFProvider {

    public RiscvObjDumpProvider(RiscvELFProvider elfprovider) {
        super(elfprovider);
    }
}