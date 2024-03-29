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

package pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.modifier;

import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.AInstructionCDFGNode;

public abstract class AInstructionCDFGModifier extends AInstructionCDFGNode{

    private String reference;
    
    protected AInstructionCDFGModifier() {
        super("");
        this.reference = "";
    }
    
    protected AInstructionCDFGModifier(String reference) {
        super("");
        this.reference = reference;
    }
    
    public void setReference(String reference) {
        this.reference = reference;
    }
    
    public String getReference() {
        return this.reference;
    }
    
    @Override
    public String toString() {
        return this.reference;
    }
}
