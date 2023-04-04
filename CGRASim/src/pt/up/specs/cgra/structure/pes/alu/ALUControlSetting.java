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

public enum ALUControlSetting implements PEControlSetting {

    PASSNULL(0b0000), // name e value para o construtor
    ADD(0b0001),
    SUB(0b0010),
    MUL(0b0011), //nao existe!
    DIV(0b0100), //nao existe!
    LSHIFT(0b0101),
    RSHIFT(0b0110),
    AND(0b0111),
    OR(0b1000),
    XOR(0b1001),
    PASSL(0b1011),
    PASSR(0b1100),
	SLT(0b1101),
	SEQ(0b1110);
	
	//mod, norm1, etc
	//end if less than
	//end if bigger than
	//end if equal
	//end if different

    private String name;
    private int value;

    private ALUControlSetting(int value) {
        this.value = value;
        this.name = name();
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
}
