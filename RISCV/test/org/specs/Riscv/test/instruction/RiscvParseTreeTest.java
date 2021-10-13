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

import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;
import org.specs.Riscv.instruction.RiscvInstruction;
import org.specs.Riscv.instruction.RiscvInstructionProperties;

import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.InstructionCDFGGenerator;
import pt.up.fe.specs.binarytranslation.lex.listeners.TreeDumper;

public class RiscvParseTreeTest {

    private void printParseTree(RiscvInstruction inst) {
        var dumper = new TreeDumper();
        var walker = new ParseTreeWalker();
        walker.walk(dumper, inst.getPseudocode().getParseTree());
    }

    @Test
    public void testCDFG() {
        var icdfg_gen = new InstructionCDFGGenerator();
        var inst = RiscvInstruction.newInstance("0", Integer.toHexString(RiscvInstructionProperties.add.getOpCode()));

        this.printParseTree(inst);

        var icdfg = icdfg_gen.generate(inst);
        icdfg.mergeInputNodes();
        icdfg.toDot("test");
    }
}
