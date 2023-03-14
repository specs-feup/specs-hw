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

import pt.up.specs.cgra.control.PEControlSetting;

public class ALUControlSettingObject implements PEControlSetting {

    /*PASSNULL(0b0000),//name e value para o construtor
    ADD(0b0001),
    SUB(0b0010),
    MUL(0b0011),
    DIV(0b0100),
    LSHIFT(0b0101),
    RSHIFT(0b0110),
    MOD(0b0111),
    AND(0b1000),
    OR(0b1001),
    XOR(0b1011),
    PASSL(0b1100),
    PASSR(0b1101);*/

    private String name;
    private int value;
    
    private String[] opsArray = {"PASSNULL", "ADD", "SUB" , 
    		"MUL", "DIV", "LSHIFT", "RSHIFT", "MOD", 
    		"AND", "OR", "XOR", "PASSL", "PASSR"};
    
    private ALUControlSetting ctrlEnum;
    

    public ALUControlSettingObject(int value) {
        this.value = value;
        this.name = opsArray[value];
        this.ctrlEnum = ALUControlSetting.valueOf(opsArray[value]);
    }
    
    public ALUControlSettingObject(ALUControlSetting ctrls)
    {
    	switch (ctrls) {
    	
    	case PASSNULL:
    		this.value = 0b0000;
    		
    		break;
    		
    	case ADD:
    		this.value = 0b0001;
    		break;
    		
    	case SUB:
    		this.value = 0b0010;
    		break;
    	
    	case MUL:
    		this.value = 0b0011;
    		break;
    	
    	case DIV:
    		this.value = 0b0100;
    		break;
    		
    	case LSHIFT:
    		this.value = 0b0101;
    		break;
    	
    	case RSHIFT:
    		this.value = 0b0110;
    		break;
    	
    	case MOD:
    		this.value = 0b0111;
    		break;
    	
    	case AND:
    		this.value = 0b1000;
    		break;
    	
    	case OR:
    		this.value = 0b1001;
    		break;
    	
    	case XOR:
    		this.value = 0b1010;
    		break;
    	
    	case PASSL:
    		this.value = 0b1011;
    		break;
    	
    	case PASSR:
    		this.value = 0b1100;
    		break;
    	}
    	
    	this.name = opsArray[value];
    }

    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public String toString() {
    	return this.name;
    }

    @Override
    public int getValue() {
        return this.value;
    }
    
    public ALUControlSetting getEnumValue()
    {
    	return this.ctrlEnum;
    }
}
