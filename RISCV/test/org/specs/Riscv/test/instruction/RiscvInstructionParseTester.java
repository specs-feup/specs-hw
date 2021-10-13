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
 
package org.specs.Riscv.test.instruction;

import org.junit.Test;
import org.specs.Riscv.instruction.RiscvInstruction;

public class RiscvInstructionParseTester {

    @Test
    public void testRiscvParse() {

        var i = RiscvInstruction.newInstance("000", "00670733");
        i.printInstruction();
    }

    @Test
    public void testRiscvOperandGetJal() {

        // 80000148: 05d060ef jal ra,800069a4 <__mulsf3>
        var i = RiscvInstruction.newInstance("80000148", "05d060ef");
        i.printInstruction();
    }

    // 80006b6c: 00008067 ret
    // this is also a "jalr" under the following fieldconditions
    // [addr: 2147511148, type: JALR, fields: {immtwelve=000000000000, rs1=00001, opcodea=000, rd=00000}]
    //
    // TODO: how to interpret this case as a "ret"?

    // "The RET translates to jalr x0, 0(ra)."

    // see: https://inst.eecs.berkeley.edu/~cs61c/resources/su18_lec/Lecture7.pdf
    // # ret and jr psuedo-instructions
    // ret = jr ra = jalr x0, ra, 0
}
