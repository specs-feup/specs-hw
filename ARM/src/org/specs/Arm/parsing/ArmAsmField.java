/**
 * Copyright 2019 SPeCS.
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

package org.specs.Arm.parsing;

import java.util.HashSet;
import java.util.Set;

import pt.up.fe.specs.binarytranslation.parsing.AsmField;

public enum ArmAsmField implements AsmField {

    COND("cond"),
    S("S"),
    N("N"),
    O("O"),
    SHIFT("shift"),
    NZCV("nzcv"),
    SF("sf"),
    SFA("sfa"),
    SFB("sfb"),
    SIMD("simd"),
    OPTION("option"),
    OPCODEA("opcodea"),
    OPCODEB("opcodeb"),
    OPCODEC("opcodec"),
    OPCODED("opcoded"),
    MEMTYPE("memtype"),
    RA("registera"),
    RM("registerm"),
    RN("registern"),
    RD("registerd"),
    RT("registert"),
    IMM("imm"),
    IMML("imml"),
    IMMH("immh"),
    IMMR("immr"),
    IMMS("imms");

    private String fieldName;

    private ArmAsmField(String fieldname) {
        this.fieldName = fieldname;
    }

    public String getFieldName() {
        return fieldName;
    }

    public static Set<String> getFields() {
        Set<String> sset = new HashSet<String>();
        for (ArmAsmField val : ArmAsmField.values()) {
            sset.add(val.getFieldName());
        }

        return sset;
    }
}
