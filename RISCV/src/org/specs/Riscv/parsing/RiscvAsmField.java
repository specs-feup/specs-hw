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

import java.util.HashSet;
import java.util.Set;

import pt.up.fe.specs.binarytranslation.asm.parsing.AsmField;

public enum RiscvAsmField implements AsmField {

    // FUNCT7,
    RS3,
    RS2,
    RS1,
    RM,
    // FUNCT3,
    RD,
    OPCODEA,
    OPCODEB,
    IMM,
    IMMTWENTY,
    IMMTWELVE,
    IMMTEN,
    IMMEIGHT,
    IMMSEVEN,
    IMMSIX,
    IMMFIVE,
    IMMFOUR,
    BIT12,
    BIT11,
    BIT20;

    // TODO: check IMM field names (can only one suffice??)

    private String fieldName;

    private RiscvAsmField() {
        this.fieldName = name().toLowerCase();
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }

    public static Set<String> getFields() {
        Set<String> sset = new HashSet<String>();
        for (RiscvAsmField val : RiscvAsmField.values()) {
            sset.add(val.getFieldName());
        }

        return sset;
    }
}
