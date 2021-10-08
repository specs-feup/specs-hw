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

package pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.edge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jgrapht.graph.DefaultEdge;

import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.edge.modifiers.AGenericCDFGModifier;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.node.AGenericCDFGNode;

public class GenericCDFGEdge extends DefaultEdge{
 
    private String condition; 
    private List<AGenericCDFGModifier> modifier;
    
    public GenericCDFGEdge() {
        this.modifier = new ArrayList<AGenericCDFGModifier>();
        this.condition = null;
    }      
    
    public GenericCDFGEdge(String condition, List<AGenericCDFGModifier> modifiers) {
        this.modifier = modifiers;
        this.condition = condition;
    }
    
    public boolean addModifiers(AGenericCDFGModifier ... modifiers) {
        return this.modifier.addAll(Arrays.asList(modifiers));
    }
    
    public boolean addModifiers(List<AGenericCDFGModifier> modifiers) {
        return this.modifier.addAll(modifiers);
    }
    
    public boolean setModifiers(AGenericCDFGModifier ... modifiers) {
        this.modifier.clear();
        return this.addModifiers(modifiers);
    }
    
    public boolean setModifiers(List<AGenericCDFGModifier> modifiers) {
        this.modifier.clear();
        return this.addModifiers(modifiers);
    }
    
    public boolean removeModifiers(AGenericCDFGModifier ... modifiers) {
        return this.modifier.removeAll(Arrays.asList(modifiers));
    }
    
    public List<AGenericCDFGModifier> getModifiers(){
        return this.modifier;
    }
    
    public void setCondition(String condition) {
        this.condition = condition;
    }
    
   public void setCondition(Boolean condition) {
       this.condition = String.valueOf(condition);
   }
   
   public void setCondition(Number condition) {
       this.condition = String.valueOf(condition);
   }
    
   public String getCondition() {       
       return (this.condition == null) ? "" : this.condition;
   }

    public GenericCDFGEdge duplicate() {
        return new GenericCDFGEdge(this.condition, this.modifier);
    }
    
    public String getDotShape() {     
        return (this.modifier.isEmpty()) ? "arrow" : "line";
    }
    
}
