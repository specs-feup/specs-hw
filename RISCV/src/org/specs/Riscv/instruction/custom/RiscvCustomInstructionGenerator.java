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

import java.util.ArrayList;
import java.util.List;

import org.specs.Riscv.parsing.RiscvAsmFieldType;

public class RiscvCustomInstructionGenerator {

    /*
     * This will only generate 32 bit instructions
     * 
     * mod_ld(module, index);
     * mod_mov_in(cpu0, mod0, cpu1, mod1, index);
     * ...
     * TODO add execute module instruction
     * mod_mov_out(mod0, cpu0, mod1, cpu1, index);
     * ...
     * 
     */
    

    
    private static String getFreeOpcode() {
        /*
         * All 32 bit opcodes need to terminate with bbb11, where bbb != 111
         */
        
    }
    
    private static final List<String> usedOpcodes = new ArrayList<>();
    
    static {
        for(RiscvAsmFieldType opcode : RiscvAsmFieldType.values()) 
            RiscvCustomInstructionGenerator.usedOpcodes.add(opcode.getFormat());
    }
    
    private static final String mod_ld_opcode = RiscvCustomInstructionGenerator.getFreeOpcode();
    private static final String mod_mov_in_opcode = RiscvCustomInstructionGenerator.getFreeOpcode();
    private static final String mod_mov_out_opcode = RiscvCustomInstructionGenerator.getFreeOpcode();
    
    private static String generateInstruction() {
        
    }
    
    // All instruction should use U-type format (more flexible for this application)
    // Instead of using func parts of the instruction, only opcodes should probably be used (more usable bits)
    
    //module[20]module_index[5]opcode[7]
    public static String generateLoadModuleInstruction(String module, String module_index) {
        return module + module_index + RiscvCustomInstructionGenerator.mod_ld_opcode;
    }
    
    
    //cpu_rs0[5]module_rd0[5]cpu_rs1[5]module_rd1[5]module_index[5]opcode[7]
    public static String generateMoveToModuleInputsInstruction(String cpu_rs0, String module_rd0, String cpu_rs1, String module_rd1, String module_index) {
        return cpu_rs0 + module_rd0 + cpu_rs1 + module_rd1 + module_index + RiscvCustomInstructionGenerator.mod_mov_in_opcode;
    }
    
    //module_rs0[5]cpu_rd0[5]module_rs1[5]cpu_rd1[5]module_index[5]opcode[7]
    public static String generateMoveFromModuleOutputsInstruction(String module_rs0, String cpu_rd0, String module_rs1, String cpu_rd1, String module_index) {
        return module_rs0 + cpu_rd0 + module_rs1 + cpu_rd1 + module_index + RiscvCustomInstructionGenerator.mod_mov_out_opcode;
    }
    
}
