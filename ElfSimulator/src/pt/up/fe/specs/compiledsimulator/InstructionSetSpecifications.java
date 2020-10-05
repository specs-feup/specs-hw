/**
 *  Copyright 2020 SPeCS.
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

package pt.up.fe.specs.compiledsimulator;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


public class InstructionSetSpecifications {
    private Map<String, Integer> registers = new HashMap<>();
    private int step;
    private String name;
    
    public InstructionSetSpecifications(File specifications) {
        //TODO Parse file into specifications
    }
    
    public InstructionSetSpecifications() {
        this.setStep(0);
        this.addRegister("r0", 0);
        this.addRegister("r1", 0);
        this.addRegister("r2", 0);
        this.addRegister("r3", 0);
        this.addRegister("r4", 0);
        this.setName("Microblaze");
    }

    
    public Map<String, Integer> getRegisters() {
        return registers;
    }

    public void addRegister(String name, int startingValue) {
        registers.put(name, startingValue);
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

 

}
