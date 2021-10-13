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

public enum RiscvBaseFormats {

    R("opcodeb(7)_rs2(5)_rs1(5)_opcodea(3)_rd(5)"),
    R4("rs3(5)_opcodeb(2)_rs2(5)_rs1(5)_rm(3)_rd(5)"),
    I("immtwelve(12)_rs1(5)_opcodea(3)_rd(5)"),
    UI("immtwenty(20)_rd(5)"),
    S("immseven(7)_rs2(5)_rs1(5)_opcodea(3)_immfive(5)"),
    B("bit12(1)_immsix(6)_rs2(5)_rs1(5)_opcodea(3)_immfour(4)_bit11(1)"),
    U("immtwenty(20)_rd(5)"),
    UJ("bit20(1)_immten(10)_bit11(1)_immeight(8)_rd(5)");

    private String format;

    private RiscvBaseFormats(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }
}