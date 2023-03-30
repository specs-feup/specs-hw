/**
 * Copyright 2023 SPeCS.
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

package pt.up.specs.cgra.structure.pes.alu;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import pt.up.specs.cgra.dataypes.PEData;
import pt.up.specs.cgra.structure.pes.ProcessingElement;
import pt.up.specs.cgra.structure.pes.binary.BinaryProcessingElement;

public class ALUElement extends BinaryProcessingElement {// implements PEControl<ALUControlSetting> {

    /*
     * helper mapping of constructors to reduce verbosity
     */
    interface ALUOperation {
        PEData apply(PEData a, PEData b);
    }

    private static final Map<ALUControlSetting, ALUOperation> ALUOperations;
    static {
        Map<ALUControlSetting, ALUOperation> amap = new HashMap<ALUControlSetting, ALUOperation>();
        amap.put(ALUControlSetting.ADD, (a, b) -> a.add(b));
        amap.put(ALUControlSetting.SUB, (a, b) -> a.sub(b));
        amap.put(ALUControlSetting.MUL, (a, b) -> a.mul(b));
        amap.put(ALUControlSetting.DIV, (a, b) -> a.div(b));
        amap.put(ALUControlSetting.LSHIFT, (a, b) -> a.lshift(b));
        amap.put(ALUControlSetting.RSHIFT, (a, b) -> a.rshift(b));
        // amap.put(ALUControlSetting.NORM1, (a, b) -> a.mul(a) + b.mul(b));
        /*amap.put(ALUControl.MOD, (a, b) -> a.mod(b));
        amap.put(ALUControl.AND, (a, b) -> a.and(b));
        amap.put(ALUControl.OR, (a, b) -> a.or(b));
        amap.put(ALUControl.XOR, (a, b) -> a.xor(b));
        amap.put(ALUControl.PASSL, (a, b) -> a.passl(b));
        amap.put(ALUControl.PASSR, (a, b) -> a.passr(b));
        amap.put(ALUControl.PASSNULL, (a, b) -> a.passnull(b));*/
        ALUOperations = Collections.unmodifiableMap(amap);
    }

    /*
     * At the next "_execute" step, this operation will be executed
     */
    private ALUControlSetting ctrl;

    public ALUElement(int latency, int memorySize) {
        super(latency, memorySize);
    }

    public ALUElement(int latency) {
        this(latency, 0);
    }

    public ALUElement() {
        this(1, 0);
    }

   public boolean setControl(ALUControlSetting ctrl) {

        if (!ALUOperations.containsKey(ctrl))
            return false;

        this.ctrl = ctrl;
        return true;
    }
    
	public boolean setControl(Integer ctrl) {
    	
    	// turn integer into valid enum of type ALUControlSetting
    	ALUControlSetting ectrl = null;
    	for(var e: ALUControlSetting.values()) {
    		ectrl = e;
    		if(ctrl == ectrl.getValue())
    			return this.setControl(ectrl);
    	}
   
    	return false;

	}

    @Override
    protected PEData _execute() {
        return ALUOperations.get(this.ctrl).apply(this.getOperand(0), this.getOperand(1));
    }

    @Override
    public ProcessingElement copy() {
        return new ALUElement(this.getLatency(), this.getMemorySize());
    }

    @Override
    public String toString() {
        return "ALU";
    }
}
