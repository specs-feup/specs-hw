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

package pt.up.fe.specs.binarytranslation.instruction.cdfg.legacy.generic.edge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.DefaultAttribute;

import pt.up.fe.specs.binarytranslation.instruction.cdfg.legacy.generic.edge.modifiers.AGenericCDFGModifier;

public class GenericCDFGDataEdge extends AGenericCDFGEdge{

    private List<AGenericCDFGModifier> modifier;
    
    public GenericCDFGDataEdge() {
        this.modifier = new ArrayList<AGenericCDFGModifier>();
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
    
    public AGenericCDFGEdge duplicate() {
        return new GenericCDFGDataEdge();
    }
    
    public Attribute getDotArrowhead() {
        return DefaultAttribute.createAttribute("line");
    }
    
    public Attribute getDotLabel() {
        
        StringBuilder label = new StringBuilder();
        
        for(AGenericCDFGModifier mod : this.getModifiers()) {
            label.append(mod.toString() + "\\n");
        }
        
        return DefaultAttribute.createAttribute(label.toString());
    }
}
