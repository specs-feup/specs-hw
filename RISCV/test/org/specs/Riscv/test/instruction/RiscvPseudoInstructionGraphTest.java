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

import pt.up.fe.specs.binarytranslation.instruction.ast.InstructionAST;

public class RiscvPseudoInstructionGraphTest {

    @Test
    public void testFast() {
        var i = RiscvInstruction.newInstance("80000948", "08060793");
        System.out.println(i);

        var ops = i.getData().getOperands();
        for (var o : ops) {
            System.out.println(o.getContainerRegister().getAsmField());
            System.out.println(o.getRepresentation());
        }
    }

    @Test
    public void testPseudoInstructionAST() {
        // 80000948: 08060793 addi a5,a2,128
        var i = RiscvInstruction.newInstance("80000948", "08060793");
        var ast = new InstructionAST(i);
        System.out.println(ast.toString());
    }

    @Test
    public void testPseudoInstructionGraphAdd() {
        // 80000948: 08060793 addi a5,a2,128
        var i = RiscvInstruction.newInstance("80000948", "08060793");
        var g = i.getPseudocodeGraph();

        System.out.println(i.getRepresentation());
        System.out.println(g.toString());

        // //80000954: 00f807b3 add a5,a6,a5
        // i = RiscvInstruction.newInstance("80000954", "00f807b3");
        // System.out.println(i.getRepresentation());
        // g = new PseudoInstructionGraph(i);
        // System.out.println(g.toString());
    }
}
