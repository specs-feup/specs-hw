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

package org.specs.MicroBlaze.test.instruction;

import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;
import org.specs.MicroBlaze.instruction.MicroBlazeInstruction;
import org.specs.MicroBlaze.instruction.MicroBlazeInstructionProperties;
import org.specs.MicroBlaze.parsing.MicroBlazeAsmFieldType;

import pt.up.fe.specs.binarytranslation.lex.listeners.TreeDumper;

public class MicroBlazeParseTreeTest {

    private void testParseTreeByASmField(MicroBlazeAsmFieldType type) {
        var dumper = new TreeDumper();
        var walker = new ParseTreeWalker();
        for (var props : MicroBlazeInstructionProperties.values()) {
            if (props.getCodeType() == type) {
                var inst = MicroBlazeInstruction.newInstance("0", Integer.toHexString(props.getOpCode()));
                System.out.println("----------------------------");
                System.out.println("--- Tree for: " + inst.getRepresentation());
                walker.walk(dumper, inst.getPseudocode().getParseTree());
            }
        }
    }

    @Test
    public void testAllMicroBlazeParseTrees() {
        this.testParseTreeByASmField(MicroBlazeAsmFieldType.UBRANCH);
        this.testParseTreeByASmField(MicroBlazeAsmFieldType.ULBRANCH);
        this.testParseTreeByASmField(MicroBlazeAsmFieldType.UIBRANCH);
    }
}
