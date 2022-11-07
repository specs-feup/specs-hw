/**
 * Copyright 2021 SPeCS.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */

package pt.up.specs.cgra.dataypes;

/**
 * Required methods for any data type which is meant to be used as data to be processed by a @ProcessingElement
 * Any PEData should only be attributed a value ONCE
 * 
 * @author nuno
 *
 */
public interface PEData {

    public PEData add(PEData operandB);

    public PEData sub(PEData operandB);

    public PEData mul(PEData operandB);

    public PEData div(PEData operandB);

    public PEData lshift(PEData operandB);

    public PEData rshift(PEData operandB);
    
    public PEData mod(PEData operandB);
    
    public PEData and(PEData operandB);
    
    public PEData or(PEData operandB);
    
    public PEData xor(PEData operandB);
    
    public PEData passl(PEData operandB);
    
    public PEData passr(PEData operandB);
    
    public PEData passnull(PEData operandB);

    public Number getValue();

    public PEData copy();

    // public PEData partSelect(PEData operandB);
}
