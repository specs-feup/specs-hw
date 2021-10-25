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

package pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.operand;

import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.DefaultAttribute;

import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.AInstructionCDFGEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.modifier.AInstructionCDFGModifier;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.modifier.subscript.AInstructionCDFGSubscriptModifier;

public abstract class AInstructionCDFGOperandEdge extends AInstructionCDFGEdge{

    private String side;
    
    private AInstructionCDFGModifier source_modifier = null;
    
    protected AInstructionCDFGOperandEdge(String side) {
        this.side = side;
    }
    
    public void setModifier(AInstructionCDFGModifier subscript) {
        this.source_modifier = subscript;
    }
    
    
    public AInstructionCDFGModifier getModifier() {
        return this.source_modifier;
    }
    
    
    @Override
    public Attribute getDOTLabel() {
        
        if(this.source_modifier != null) {
            return DefaultAttribute.createAttribute(this.side + "\\n" + this.getModifier());
        }
        
        return DefaultAttribute.createAttribute(this.side);
    }
    
}
