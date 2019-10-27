/**
 *  Copyright 2019 SPeCS.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.specs.MicroBlaze.test;

import org.junit.Test;
import org.specs.MicroBlaze.instruction.MicroBlazeInstruction;
import org.specs.MicroBlaze.instruction.MicroBlazeInstructionExpression;

import pt.up.fe.specs.binarytranslation.expression.ExpressionSymbol;

public class MicroBlazeInstructionExpressionTest {

    @Test
    public void test() {
        MicroBlazeInstruction inst = MicroBlazeInstruction.newInstance("00000000", "00000000");      
        System.out.print(MicroBlazeInstructionExpression.add.express((inst.getData().getOperands())));
    }
}
