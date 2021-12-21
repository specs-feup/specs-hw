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

package org.specs.MicroBlaze.test.lex;

import java.util.ArrayList;

import org.junit.Test;
import org.specs.MicroBlaze.instruction.MicroBlazeInstruction;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;

public class MicroBlazeLexTester {

    /* Snippet of code from "lin_recurrence_init" in MicroBlazeLivermoreN100
     * 
     *     385c:    12c00000    addk    r22, r0, r0
        3860:   336003bd    addik   r27, r0, 957
        3864:   33400004    addik   r26, r0, 4
        3868:   111b0000    addk    r8, r27, r0
        386c:   10fc0000    addk    r7, r28, r0
        3870:   10da0000    addk    r6, r26, r0
     */

    @Test
    public void test() {

        String rawinsts[] = { "12c00000", "336003bd", "33400004", "111b0000", "10fc0000", "10da0000" };

        // instructions
        int i = 0;
        var instlist = new ArrayList<Instruction>();
        for (var s : rawinsts) {
            instlist.add(MicroBlazeInstruction.newInstance(String.valueOf(i), s));
            i += 4;
        }

        // pseudoinsts
        var pseudolist = new ArrayList<String>();
        for (var inst : instlist) {
            var pseudocode = inst.getPseudocode().getCode();
            pseudolist.add(pseudocode);
        }

        // TODO : apply SSA and perform transformations so that the tree expresses data dependency (?)

        // convert to tree
        for (var pseudo : pseudolist) {
            System.out.println(pseudo.toString());
        }
    }

}
