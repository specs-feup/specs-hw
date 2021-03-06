package org.specs.Riscv.parsing;

import pt.up.fe.specs.binarytranslation.asm.parsing.AsmFieldType;

public class RiscvAsmFieldTypeV2 implements AsmFieldType {

    private RiscvBaseFormats base;
    private RiscvAsmFieldType maincode;

    public RiscvAsmFieldTypeV2(RiscvBaseFormats base, RiscvAsmFieldType maincode) {
        this.base = base;
        this.maincode = maincode;
    }

    public RiscvBaseFormats getBase() {
        return base;
    }

    public RiscvAsmFieldType getMaincode() {
        return maincode;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == this) {
            return true;
        }

        if (!(obj instanceof RiscvAsmFieldTypeV2)) {
            return false;
        }

        var cast = (RiscvAsmFieldTypeV2) obj;
        return (cast.getBase() == this.getBase() && cast.getMaincode() == this.getMaincode());
    }
}
