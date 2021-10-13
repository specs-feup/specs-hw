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

package pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.edge.modifiers;

public class GenericCDFGPartSelectModifier extends AGenericCDFGModifier{

    private int lower;
    private int upper;
    
   public GenericCDFGPartSelectModifier() {
       super( GenericCDFGModifierType.PartSelect.getType());
   }
    
    public GenericCDFGPartSelectModifier(int lower_bound, int upper_bound) {  
        
        this();
        
        this.lower = lower_bound;
        this.upper = upper_bound;
        
    }

    public int getLowerBound() {
        return this.lower;
    }
    
    public int getUpperBound() {
        return this.upper;
    }
    
    public void setLowerBound(int lower_bound) {
        this.lower = lower_bound;
    }
    
    public void setUpperBound(int upper_bound) {
        this.upper = upper_bound;
    }
    
    @Override
    public String toString() {
        return "[" + String.valueOf(this.lower) + ":" + String.valueOf(this.upper) +"]";
    }
    
    @Override
    public AGenericCDFGModifier generate() {
        return new GenericCDFGPartSelectModifier();
    }
}
