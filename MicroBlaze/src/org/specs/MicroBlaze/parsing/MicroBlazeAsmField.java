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

package org.specs.MicroBlaze.parsing;

import java.util.HashSet;
import java.util.Set;

import pt.up.fe.specs.binarytranslation.asm.parsing.AsmField;

public enum MicroBlazeAsmField implements AsmField {

    OPCODEA("opcodea"),
    OPCODEB("opcodeb"),
    OPCODEC("opcodec"),
    OPCODED("opcoded"),
    RD("registerd"),
    RA("registera"),
    RB("registerb"),
    RS("registers"),
    IMM("imm"),
    IMMW("immw");

    private String fieldName;

    private MicroBlazeAsmField(String fieldname) {
        this.fieldName = fieldname;
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }

    public static Set<String> getFields() {
        Set<String> sset = new HashSet<String>();
        for (MicroBlazeAsmField val : MicroBlazeAsmField.values()) {
            sset.add(val.getFieldName());
        }

        return sset;
    }
}
