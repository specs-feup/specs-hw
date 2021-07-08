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

package org.specs.MicroBlaze.test.instruction;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.specs.MicroBlaze.instruction.MicroBlazeInstruction;

import pt.up.fe.specs.binarytranslation.analysis.graphs.pseudocode.PseudoInstructionEvaluator;
import pt.up.fe.specs.binarytranslation.analysis.graphs.pseudocode.PseudoInstructionGraph;

public class MicroBlazePseudoInstructionGraphTest {
    @Test
    public void testPseudoInstructionGraphAdd() {
        // 248: 20c065e8 addi r6, r0, 26088 // 65e8 <_SDA_BASE_>
        var i = MicroBlazeInstruction.newInstance("248", "20c065e8");
        var g = i.getPseudocodeGraph();
        
        System.out.println(i.getRepresentation());
        System.out.println(g.toString());
        
        //addk   r6,  r8,  r4: 10c82000
        i = MicroBlazeInstruction.newInstance("248", "10c82000");
        g = i.getPseudocodeGraph();
        
        System.out.println(i.getRepresentation());
        System.out.println(g.toString());
    }
    
    @Test
    public void testPseudoInstructionGraphCmp() {
        //cmp    r18,  r9,  r4: 16492001
        var i = MicroBlazeInstruction.newInstance("248", "16492001");
        var g = i.getPseudocodeGraph();
        
        System.out.println(i.getRepresentation());
        System.out.println(g.toString());
    }
    
    @Test
    public void testEval()  {
        // 248: 20c065e8 addi r6, r0, 26088 // 65e8 <_SDA_BASE_>
        var i = MicroBlazeInstruction.newInstance("248", "20c065e8");        
        var g = i.getPseudocodeGraph();
        
        Map<String, Number> inputs = new HashMap<>();
        inputs.put("r0", 0);
        
        var evaluator = new PseudoInstructionEvaluator(g);
        var res = evaluator.eval(inputs);
        Assert.assertEquals(res.longValue(), 26088L);
        
        inputs.put("r0", 2.0);
        res = evaluator.eval(inputs);
        Assert.assertEquals(res.longValue(), 26090L);
    }
}
