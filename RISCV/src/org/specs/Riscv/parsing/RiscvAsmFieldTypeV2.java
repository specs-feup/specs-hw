/**
 * Copyright 2021 SPeCS.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */
 
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
