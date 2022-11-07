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

import pt.up.specs.cgra.dataypes.PEControl;
import pt.up.specs.cgra.dataypes.PEControlALU;

import pt.up.specs.cgra.dataypes.PEData;

public class ALUElement extends BinaryProcessingElement {

    // TODO: datatypes for T? bitwidths?


	public ALUElement(int latency, int memorySize) {
        super(latency, memorySize);
    }

    public ALUElement(int latency) {
        this(latency, 0);
    }

    public ALUElement() {
        this(1, 0);
    }
    
	public PEControlALU getControl() {
		return control;
	}

	public void setControl(PEControlALU control) {
		this.control = control;
	}

    

    @Override
    protected PEData _execute() {
        switch(control.getOperation())
        {
        case ADD:
        	return this.getOperand(0).add(this.getOperand(1));
        	
		case SUB:
        	return this.getOperand(0).sub(this.getOperand(1));
        	
        case MUL:
        	return this.getOperand(0).mul(this.getOperand(1));
        	
        case DIV:
        	return this.getOperand(0).div(this.getOperand(1));
        	
        case LSHIFT:
        	return this.getOperand(0).lshift(this.getOperand(1));
        	
        case RSHIFT:
        	return this.getOperand(0).rshift(this.getOperand(1));
        	
        case MOD:        	
        	return this.getOperand(0).mod(this.getOperand(1));

        case AND:        	
        	return this.getOperand(0).and(this.getOperand(1));

        case OR:        	
        	return this.getOperand(0).or(this.getOperand(1));

        case XOR:        	
        	return this.getOperand(0).xor(this.getOperand(1));

        case PASSL:        	
        	return this.getOperand(0).passl(this.getOperand(1));

        case PASSR:        	
        	return this.getOperand(0).passr(this.getOperand(1));
        	
        	
        case PASSNULL:
        	
        default:
        	return this.getOperand(0).passnull(this.getOperand(1));
        
        }
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
