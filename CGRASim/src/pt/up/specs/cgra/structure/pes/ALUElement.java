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
 
package pt.up.specs.cgra.structure.pes;

import pt.up.specs.cgra.dataypes.PEData;

public class ALUElement extends BinaryProcessingElement {

    // TODO: datatypes for T? bitwidths?
	String type;

    public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ALUElement(int latency, int memorySize) {
        super(latency, memorySize);
    }

    public ALUElement(int latency) {
        this(latency, 0);
    }

    public ALUElement() {
        this(1, 0);
    }

    @Override
    protected PEData _execute() {
        switch(this.type)
        {
        case "add":
        	return this.getOperand(0).add(this.getOperand(1));
        	
		case "sub":
        	return this.getOperand(0).sub(this.getOperand(1));
        	
        case "mul":
        	return this.getOperand(0).mul(this.getOperand(1));
        	
        case "div":
        	return this.getOperand(0).div(this.getOperand(1));
        	
        case "lshift":
        	return this.getOperand(0).lshift(this.getOperand(1));
        	
        case "rshift":
        	return this.getOperand(0).rshift(this.getOperand(1));
        	
        case "mod":        	
        	return this.getOperand(0).mod(this.getOperand(1));

        case "AND":        	
        	return this.getOperand(0).AND(this.getOperand(1));

        case "OR":        	
        	return this.getOperand(0).OR(this.getOperand(1));

        case "XOR":        	
        	return this.getOperand(0).XOR(this.getOperand(1));

        case "passL":        	
        	return this.getOperand(0).passL(this.getOperand(1));

        case "passR":        	
        	return this.getOperand(0).passR(this.getOperand(1));
        	
        	
        case "passNull":
        	
        default:
        	return this.getOperand(0).passNull(this.getOperand(1));
        
        }
    }

    @Override
    public ProcessingElement copy() {
        return new ALUElement(this.getLatency(), this.getMemorySize());
    }

    @Override
    public String toString() {
        return "Adder";
    }
    
}
