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

package pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.data;

public class InstructionCDFGLiteralNode extends AInstructionCDFGDataNode{

    public InstructionCDFGLiteralNode(String reference) {
        super(reference);
    }
    
    public InstructionCDFGLiteralNode(Number reference) {
        super(String.valueOf(reference));
    }

    public Number getValue() {
        return Integer.valueOf(super.getReference());
    }
    
    @Override
    public String getReference() {
        return "I_" + super.getReference().replace("-", "n");
    }
    
}
