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

package pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.instruction.edge.operand;

import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.DefaultAttribute;

import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.instruction.edge.AInstructionCDFGEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.instruction.modifier.*;

public abstract class AInstructionCDFGOperandEdge extends AInstructionCDFGEdge{

    private String side;
    
    private AInstructionCDFGSubscriptModifier source_subscript = null;
    
    protected AInstructionCDFGOperandEdge(String side) {
        this.side = side;
    }
    
    public void setSubscript(AInstructionCDFGSubscriptModifier subscript) {
        this.source_subscript = subscript;
    }
    
    public void setSubscript(int upper, int lower) {
        this.source_subscript = new InstructionCDFGRangeSubscript(upper, lower);
    }
    
    public void setSubscript(int bit) {
        this.source_subscript = new InstructionCDFGScalarSubscript(bit);
    }
    
    public AInstructionCDFGSubscriptModifier getSubscript() {
        return this.source_subscript;
    }
    
    
    @Override
    public Attribute getDOTLabel() {
        
        if(source_subscript != null) {
            return DefaultAttribute.createAttribute(this.side + "\\n" + this.getSubscript());
        }
        
        return DefaultAttribute.createAttribute(this.side);
    }
    
}
