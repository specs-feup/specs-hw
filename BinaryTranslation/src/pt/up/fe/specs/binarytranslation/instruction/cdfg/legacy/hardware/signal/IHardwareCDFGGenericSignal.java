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

package pt.up.fe.specs.binarytranslation.instruction.cdfg.legacy.hardware.signal;

public interface IHardwareCDFGGenericSignal<ImplementClass> {

    public boolean isWire();
    public ImplementClass setAsWire();
    
    public boolean isReg();
    public ImplementClass setAsReg();
    
    public boolean isSigned();
    public ImplementClass setAsSigned();
    
    public boolean isUnsigned();
    public ImplementClass setAsUnsigned();
    
    public int getBitWidth();
    public ImplementClass setBitWidth(int bitwidth);
    
    public int getUpperBound();
    public ImplementClass setUpperBound(int bound);
    
    public int getLowerBound();
    public ImplementClass setLowerBound(int bound);
    
}
