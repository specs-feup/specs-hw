/**
 *  Copyright 2021 SPeCS.
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

package pt.up.fe.specs.binarytranslation.test.instruction.dfg;

import org.junit.Test;

import pt.up.fe.specs.binarytranslation.instruction.dfg.nodes.InstructionDFGNodeType;
import pt.up.fe.specs.binarytranslation.instruction.dfg.nodes.base.data.InstructionDFGNodeDataType;
import pt.up.fe.specs.binarytranslation.instruction.dfg.nodes.base.operation.math.InstructionDFGMathOperationNodeType;

public class InstructionDFGNodeTypeTestUtils {

    @Test
    public void checkDataNodeTypes() {
        
        for(InstructionDFGNodeDataType t: InstructionDFGNodeDataType.values())
            System.out.println(t);
        
    }
    
    @Test
    public void checkNodeTypes() {
        
        for(InstructionDFGNodeType t: InstructionDFGNodeType.values())
            System.out.println(t);
        
    }
    
    @Test
    public void checkMathOperationgetType() {
        System.out.println(InstructionDFGMathOperationNodeType.getType("a"));
    }
}
