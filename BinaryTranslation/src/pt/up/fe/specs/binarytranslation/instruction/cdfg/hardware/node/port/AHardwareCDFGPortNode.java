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

package pt.up.fe.specs.binarytranslation.instruction.cdfg.hardware.node.port;

import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.node.data.AGenericCDFGDataNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.hardware.signal.HardwareCDFGGenericSignal;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.hardware.signal.IHardwareCDFGGenericSignal;

public abstract class AHardwareCDFGPortNode extends AGenericCDFGDataNode implements IHardwareCDFGGenericSignal<AHardwareCDFGPortNode>{

    private HardwareCDFGGenericSignal signal;
    private boolean input;
    
    protected AHardwareCDFGPortNode(String reference, boolean is_input) {  
        super(reference,null);
        this.signal = new HardwareCDFGGenericSignal();    
        
        this.input = is_input;
    }
    
    public boolean isInput() {
        return (this.input == true);
    }
    
    public boolean isOutput() {
        return (this.input == false);
    }
    
    public boolean isWire() {
        return this.signal.isWire();
    }
    
    public AHardwareCDFGPortNode setAsWire() {
        this.signal.setAsWire();
        return this;
    }
    
    public boolean isReg() {
        return this.signal.isReg();
    }
    
    public AHardwareCDFGPortNode setAsReg(){
        this.signal.setAsReg();
        return this;
    }
    
    public boolean isSigned() {
        return this.signal.isSigned();
    }
    
    public AHardwareCDFGPortNode setAsSigned(){
        this.signal.setAsSigned();
        return this;
    }
    
    public boolean isUnsigned() {
        return this.signal.isUnsigned();
    }
    
    public AHardwareCDFGPortNode setAsUnsigned(){
        this.signal.setAsUnsigned();
        return this;
    }
    
    public int getBitWidth() {
        return this.signal.getBitWidth();
    }
    
    public AHardwareCDFGPortNode setBitWidth(int bitwidth){
        this.signal.setBitWidth(bitwidth);
        return this;
    }
    
    public int getUpperBound() {
        return this.signal.getUpperBound();
    }
    
    public AHardwareCDFGPortNode setUpperBound(int bound){
        this.signal.setUpperBound(bound);
        return this;
    }
    
    public int getLowerBound() {
        return this.signal.getLowerBound();
    }
    
    public AHardwareCDFGPortNode setLowerBound(int bound){
        this.signal.setLowerBound(bound);
        return this;
    }
    
}
