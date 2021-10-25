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

package pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node;

import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.DefaultAttribute;

public abstract class AInstructionCDFGNode{

    private String reference;
    private String uid = "";
    
    protected AInstructionCDFGNode (String reference) {
        this.reference = reference;
    }
    
    public String getReference() {
        return this.reference;
    }
    
    public void setUID(String uid) {
        this.uid = uid;
    }
    
    public String getUIDVal() {
        return this.uid;
    }
    
    public String getUID() {
        return this.getReference() + this.uid;
    }
    @Override
    public String toString() {
        return this.getReference();
    }
    
    public Attribute getDOTLabel() {
        return DefaultAttribute.createAttribute(this.getReference());
    }
    
    public Attribute getDOTShape() {
        return DefaultAttribute.createAttribute("circle");
    }
    
    /*
    @Override
    public boolean equals(Object obj) {
        
        if(obj instanceof AInstructionCDFGNode) {
            return (this.hashCode() == ((AInstructionCDFGNode) obj).hashCode());
        }
        
        return false;
    }
    
    
    @Override
    public int hashCode() {
        return this.getReference().hashCode();
    }*/
}
