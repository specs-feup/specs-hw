/**
 * Copyright 2022 SPeCS.
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

package pt.up.fe.specs.crispy.lib.riscv;

import pt.up.fe.specs.crispy.ast.expression.operator.Immediate;

public enum RISCVALUCodes {

    ALU_ADD(0),
    ALU_SUB(1),
    ALU_AND(2),
    ALU_OR(3),
    ALU_XOR(4),
    ALU_SLT(5),
    ALU_SLL(6),
    ALU_SLTU(7),
    ALU_SRL(8),
    ALU_SRA(9),
    ALU_COPY_A(10),
    ALU_COPY_B(11),
    ALU_XXX(15);

    private Immediate code;

    private RISCVALUCodes(Number value) {
        this.code = new Immediate(value, 4);
    }

    public Immediate getCode() {
        return code;
    }
}
