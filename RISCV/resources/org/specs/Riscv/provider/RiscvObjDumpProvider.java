package org.specs.Riscv.provider;

import pt.up.fe.specs.binarytranslation.ObjDumpProvider;

public class RiscvObjDumpProvider extends ObjDumpProvider implements RiscvELFProvider {

    public RiscvObjDumpProvider(RiscvELFProvider elfprovider) {
        super(elfprovider);
    }
}