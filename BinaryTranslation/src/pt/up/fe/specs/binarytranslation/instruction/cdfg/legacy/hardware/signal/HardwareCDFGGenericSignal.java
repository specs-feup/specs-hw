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

public class HardwareCDFGGenericSignal {
    
    private boolean wire;
    private boolean signed;
    private int upper;
    private int lower;
    
    public HardwareCDFGGenericSignal() {
        this.wire = true;
        this.signed = true;
        this.upper = 0;
        this.lower = 0;
    }
    
    public boolean isWire() {
        return (this.wire == true);
    }
    
    public void setAsWire() {
        this.wire = true;
    }
    
    public boolean isReg() {
        return (this.wire == false);
    }
    
    public void setAsReg() {
        this.wire = false;
    }
    
    public boolean isSigned() {
        return (this.signed == true);
    }
    
    public void setAsSigned() {
        this.signed = true;
    }
    
    public boolean isUnsigned() {
        return (this.signed == false);
    }
    
    public void setAsUnsigned() {
        this.signed = false;
    }
    
    public int getBitWidth() {
        return this.upper - this.lower;
    }
    
    public void setBitWidth(int bitwidth) {
        this.upper = bitwidth - 1;
        this.lower = 0;
    }
    
    public int getUpperBound() {
        return this.upper;
    }
    
    public void setUpperBound(int bound) {
        this.upper = bound;
    }
    
    public int getLowerBound() {
        return this.lower;
    }
    
    public void setLowerBound(int bound){
        this.lower = bound;
    }
    
}