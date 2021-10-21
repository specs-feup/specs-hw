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

import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.DefaultAttribute;

public class GenericCDFGControlEdge extends AGenericCDFGEdge{

    private String condition;
    
    public GenericCDFGControlEdge(String condition) {
        this.condition = condition;
    }
    
    public GenericCDFGControlEdge(Boolean condition) {
        this.condition = String.valueOf(condition);
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
       return this.condition;
   }
 
   public AGenericCDFGEdge duplicate() {
       return new GenericCDFGControlEdge(this.getCondition());
   }
   
   public Attribute getDotArrowhead() {
       return DefaultAttribute.createAttribute("arrow");
   }
   
   public Attribute getDotLabel() {
       return DefaultAttribute.createAttribute(this.getCondition());
   }
   
}
