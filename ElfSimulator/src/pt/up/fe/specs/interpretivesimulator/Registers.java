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

package pt.up.fe.specs.interpretivesimulator;

import java.util.HashMap;
import java.util.Map;

public class Registers {
    
    private Map<String, Integer> registers;
    private int programCounter;
    private boolean jumped;
    
    
    
    public Registers() {
        this.registers = new HashMap<>();
        this.programCounter = 0;
        this.jumped = false;
    }

    public void setRegister(String name, int value) {
        if(name == "$pc") programCounter = value;
        else registers.put(name, value);
    }
    
    public int getRegister(String name) {
        if(!registers.containsKey(name)) registers.put(name, 0); 
        return registers.get(name);
    }

    public int getProgramCounter() {
        return programCounter;
    }

    public void setProgramCounter(int programCounter) {
        this.programCounter = programCounter;
    }

    public void updateProgramCounter(int instructionWidth) {
        if(jumped) jumped = false;
        else programCounter += instructionWidth;
    }
}
