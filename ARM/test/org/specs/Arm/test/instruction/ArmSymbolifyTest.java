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
 
package org.specs.Arm.test.instruction;

import org.junit.Test;

public class ArmSymbolifyTest {

    @Test
    public void test() {

        // aa0203f7
        // 1_010101_000_0_00010_000000_11111_10111
        // sf = 1
        // shift = 000
        // m = 2
        // imm = 0
        // n = 32 --> sp
        // d = 23
        // ArmInstruction i = ArmInstruction.newInstance("0", "aa0203f7");

        // 9277f821
        // and x1, x1, #0xfffffffffffffeff
        // ArmInstruction i = ArmInstruction.newInstance("0", "9277f821");
        // i.printInstruction();
        /*
        ArmInstruction i = ArmInstruction.newInstance("0", "58002a60");
        i.printInstruction();
        
        char c = 'a';
        Map<String, String> regremap = new HashMap<String, String>();
        for (Operand op : i.getData().getOperands()) {
        
            if (op.isSpecial())
                continue;
        
            if (regremap.containsKey(op.getRepresentation()))
                continue;
        
            regremap.put(op.getRepresentation(), op.getPossibleSymbolicRepresentation(String.valueOf(c)));
            c++;
        }
        
        var list = new ArrayList<Instruction>();
        list.add(i);
        HashedSequence h1 = new HashedSequence(0, list, regremap);
        h1.makeSymbolic();
        */
    }

}
