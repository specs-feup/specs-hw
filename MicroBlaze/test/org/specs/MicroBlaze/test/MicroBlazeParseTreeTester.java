/**
 * Copyright 2020 SPeCS.
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

package org.specs.MicroBlaze.test;

import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;
import org.specs.MicroBlaze.instruction.MicroBlazeInstruction;

import pt.up.fe.specs.binarytranslation.instruction.ast.InstructionAST;
import pt.up.fe.specs.binarytranslation.lex.listeners.TreeDumper;

public class MicroBlazeParseTreeTester {

    @Test
    public void dumpTree() {
        // 248: 20c065e8 addi r6, r0, 26088 // 65e8 <_SDA_BASE_>
        var addi = MicroBlazeInstruction.newInstance("248", "20c065e8");
        var dumper = new TreeDumper();
        var walker = new ParseTreeWalker();
        walker.walk(dumper, addi.getPseudocode().getParseTree());
    }

    @Test
    public void testAST() {
        // 248: 20c065e8 addi r6, r0, 26088 // 65e8 <_SDA_BASE_>
        var addi = MicroBlazeInstruction.newInstance("248", "20c065e8");
        var ast = new InstructionAST(addi);
        System.out.println(ast.getRootnode().getAsString());
    }
}
