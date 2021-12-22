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

package pt.up.fe.specs.binarytranslation.hardware.validation.generator.operation.ternary.subscript;

public class HardwareValidationRangeSubscript extends AHardwareValidationTernarySubscript{

    private Number generateMask(Number upper, Number lower) {
        
        Integer mask = 0;
        
        for(int i = lower.intValue(); i < upper.intValue(); i++) {
            mask += (1<<i);
        }
        
        return mask;
    }
    
    @Override
    public Number apply(Number variable, Number upper, Number lower) {
        return variable.intValue() & this.generateMask(upper, lower).intValue();
    }

}
