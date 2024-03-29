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

package pt.up.fe.specs.binarytranslation.instruction.cdfg.legacy.generic.edge.modifiers;

public class GenericCDFGBitSelectModifier extends AGenericCDFGModifier{

    private int bit;
    
    public GenericCDFGBitSelectModifier() {
        super(GenericCDFGModifierType.BitSelect.getType());
    }
    
    public GenericCDFGBitSelectModifier(int bit) {
        this();
        
        this.bit = bit;
    }
    
    public void setBit(int bit) {
        this.bit = bit;
    }
    
    public int getBit() {
        return this.bit;
    }
    
    @Override
    public String toString() {
        return "[" + String.valueOf(this.bit) + "]";
    }
    
    @Override 
    public AGenericCDFGModifier generate() {
        return new GenericCDFGBitSelectModifier();
    }
    
}
