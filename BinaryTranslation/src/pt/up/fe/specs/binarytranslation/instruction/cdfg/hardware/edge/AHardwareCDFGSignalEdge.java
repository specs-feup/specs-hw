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

package pt.up.fe.specs.binarytranslation.instruction.cdfg.hardware.edge;

import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.edge.AGenericCDFGEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.hardware.signal.HardwareCDFGGenericSignal;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.hardware.signal.IHardwareCDFGGenericSignal;

public abstract class AHardwareCDFGSignalEdge extends AGenericCDFGEdge implements IHardwareCDFGGenericSignal<AHardwareCDFGSignalEdge>{
    
    private HardwareCDFGGenericSignal signal;
    
    protected AHardwareCDFGSignalEdge() {  
        this.signal = new HardwareCDFGGenericSignal();    
    }
    
    public boolean isWire() {
        return this.signal.isWire();
    }
    
    public AHardwareCDFGSignalEdge setAsWire() {
        this.signal.setAsWire();
        return this;
    }
    
    public boolean isReg() {
        return this.signal.isReg();
    }
    
    public AHardwareCDFGSignalEdge setAsReg(){
        this.signal.setAsReg();
        return this;
    }
    
    public boolean isSigned() {
        return this.signal.isSigned();
    }
    
    public AHardwareCDFGSignalEdge setAsSigned(){
        this.signal.setAsSigned();
        return this;
    }
    
    public boolean isUnsigned() {
        return this.signal.isUnsigned();
    }
    
    public AHardwareCDFGSignalEdge setAsUnsigned(){
        this.signal.setAsUnsigned();
        return this;
    }
    
    public int getBitWidth() {
        return this.signal.getBitWidth();
    }
    
    public AHardwareCDFGSignalEdge setBitWidth(int bitwidth){
        this.signal.setBitWidth(bitwidth);
        return this;
    }
    
    public int getUpperBound() {
        return this.signal.getUpperBound();
    }
    
    public AHardwareCDFGSignalEdge setUpperBound(int bound){
        this.signal.setUpperBound(bound);
        return this;
    }
    
    public int getLowerBound() {
        return this.signal.getLowerBound();
    }
    
    public AHardwareCDFGSignalEdge setLowerBound(int bound){
        this.signal.setLowerBound(bound);
        return this;
    }
}
