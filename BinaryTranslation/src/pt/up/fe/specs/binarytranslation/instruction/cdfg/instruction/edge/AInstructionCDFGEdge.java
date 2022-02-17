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

package pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.DefaultAttribute;

import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.modifier.AInstructionCDFGModifier;

public abstract class AInstructionCDFGEdge extends DefaultEdge {

    protected List<AInstructionCDFGModifier> source_modifier;
    
    public AInstructionCDFGEdge() {
        this.source_modifier = new ArrayList<>();
    }
    
    public Attribute getDOTArrowHead() {
        return DefaultAttribute.createAttribute("normal");
    }
    
    public Attribute getDOTArrowTail() {
        return DefaultAttribute.createAttribute("none");
    }
    
    public Map<String, Attribute> getDOTAttributeMap(){
        Map<String, Attribute> map = new LinkedHashMap<>();
        
        map.put("label", this.getDOTLabel()); 
        map.put("arrowhead", this.getDOTArrowHead());
        map.put("arrowtail", this.getDOTArrowTail());
        map.put("dir", DefaultAttribute.createAttribute("both"));
    
        return map;
    }
    
    public abstract AInstructionCDFGEdge duplicate();
    
    public void setModifier(AInstructionCDFGModifier modifier) {
        this.source_modifier.add(modifier);
    }
    
    
    public List<AInstructionCDFGModifier> getModifiers() {
        return this.source_modifier;
    }
    
    public Attribute getDOTLabel() {
        
        if(!this.source_modifier.isEmpty()) {
            
            StringBuilder modifiers = new StringBuilder();
            
            this.source_modifier.forEach(m -> modifiers.append(m.toString()+"\\n"));
            
            return DefaultAttribute.createAttribute(modifiers.toString());
        }
        
        return DefaultAttribute.createAttribute("");
    }
}
