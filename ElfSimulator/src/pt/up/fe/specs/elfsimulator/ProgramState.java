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

package pt.up.fe.specs.elfsimulator;

import java.util.HashMap;
import java.util.Map;

public class ProgramState {
    private int programCounter;
    private Map<String, Integer> registers;
    
    public ProgramState() {
        setProgramCounter(0);
        registers = new HashMap<>();
    }
    
    public int getProgramCounter() {
        return programCounter;
    }

    public void setProgramCounter(int programCounter) {
        this.programCounter = programCounter;
    }
    
    public void setRegister(String name, int value) {
        if(this.registers.put(name, value) == null)
            System.out.print(name + " register initiated");
    }
    
    public int getRegister(String name) {
        if(!this.registers.containsKey(name)) {
            System.err.print("Access to non initiated register: " + name);
            return 0;
        }
        else return this.registers.get(name);
  
    }
    
  
}
