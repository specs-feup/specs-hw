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

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.DefaultAttribute;

import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.modifier.AInstructionCDFGModifier;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.AInstructionCDFGNode;

public class AInstructionCDFGDataNode extends AInstructionCDFGNode{

    private List<AInstructionCDFGModifier> modifier;    // List of the data node's modifiers
    
    protected AInstructionCDFGDataNode(String reference) {
        super(reference);
        this.modifier = new ArrayList<>();
    }
    
    protected AInstructionCDFGDataNode(String reference, AInstructionCDFGModifier modifier) {
        this(reference);
        this.modifier.add(modifier);
    }

    /** Adds a modifiers to the data node. Currently no check of oposing modifiers is checked (such as declaring the node is signed and unsigned).
     * 
     * In a future revision this will probably throw an Exception when that occurs
     *  
     * @param modifier Modifier to be added
     */
    public void setModifier(AInstructionCDFGModifier modifier) {
        this.modifier.add(modifier);
    }
    
    /** Returns the data node's modifiers
     * 
     * @return A List of the data node's modifiers
     */
    public List<AInstructionCDFGModifier> getModifiers() {
        return this.modifier;
    }
    

    
    
    
}
