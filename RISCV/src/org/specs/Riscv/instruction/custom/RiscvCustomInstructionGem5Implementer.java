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

package org.specs.Riscv.instruction.custom;

public class RiscvCustomInstructionGem5Implementer {

    /*
     * Modify decoder.isa of gem5 instalation
     *  Maybe create copy of existing decoder.isa
     *  
     *  The issue is that gem5 uses custom pseudo code, so a gem5 pseudo code generator is needed
     *  
     */
    
    private String gem5Path;
    private final static String decoderISAPath = "/build/RISCV/arch/riscv/isa";
    
    public RiscvCustomInstructionGem5Implementer(String gem5Path) {
        this.gem5Path = gem5Path;
    }
    
    public void generate() {
        
    }
    
    public void build() {
        
    }
    
    public void reset() {
        
    }
}
