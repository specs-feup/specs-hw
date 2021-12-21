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
import java.util.Random;

import org.specs.Riscv.parsing.RiscvAsmFieldType;

public class RiscvCustomInstructionGenerator {

    /*
     * This will only generate 32 bit instructions
     * 
     * EXECUTION FLOW:
     * 
     * mod_ld(module, index);
     * mod_mov_in(cpu0, mod0, cpu1, mod1, index);
     * ...
     * mod_exec(index);
     * mod_mov_out(mod0, cpu0, mod1, cpu1, index);
     * ...
     * 
     */
    
    /* Assuming that the system allows for multiple instruction segment accelerators (needed because loading bitstreams of FPGAs takes a long time, and in multiple thread/core systems it makes sense),
     * this type of instruction has a field named index that represents the index of the module, so in this case, 32 modules can be used in parallel
     * 
     * Maybe instructions should be merged so that they use less opcodes (one ideally), but the fact that the mod_mov instructions move 2 registers at a time makes the using of modules in theory twice as fast as single mov instructions
     * Another idea would be to have a data array that represents a mapping between the cpu and the module, and in that case, if there is a bus that is 32*32bit wide it can move in/out all registers in a single clock cycle
     *  This would probably need to be done by using a modified load instruction (a vector load instruction), and the circuitry would probably be quite complex 
     *  
     *  The issue with any of the approaches that I have thought about is the size of the registers needed for the module, assuming that all of the registers are used by the module:
     *      2 * 32 * 32 bit (256B) total register size is required.
     *      if 32 modules are used:
     *      2 * 32 * 32 * 32 bit (8KB)
     *      
     *      The minimum in theory would be 1 in and 1 out register per module, so:
     *      
     *      2 * 32 * 1 * 32bit (256B)
     */

    /** Gets a free opcode to implement the desired custom instruction
     *  This is done by a really inefficient method, were random opcodes are generated and they are compared to the used ones,
     *      and if the generated opcode is not allocated it is considered valid.
     *      
     *  It adds the generated opcode to the usedOpcodes List    
     * @return Free opcode
     */
    private static String getFreeOpcode() {
        
        /*
         * All 32 bit opcodes need to terminate with bbb11, where bbb != 111
         */
        
        int base = 0x03;    // All 32 bit instructions need to end with 11
        int mask = 0x7F;    // All 32 bit instruction have a 7 bit opcode
        
        int candidate = 0;
        
        Random candidateGenerator = new Random();
        
        while(true) {
            candidate = candidateGenerator.nextInt(mask) | base ;
            if(!usedOpcodes.contains(String.valueOf(candidate))) {
                break;
            }
        }
        
        usedOpcodes.add(String.valueOf(candidate));

        return String.valueOf(candidate);
    }
    
    private static final List<String> usedOpcodes = new ArrayList<>();
    
    static {
        for(RiscvAsmFieldType opcode : RiscvAsmFieldType.values()) 
            RiscvCustomInstructionGenerator.usedOpcodes.add(opcode.getFormat());
    }
    
    private static final String mod_ld_opcode = RiscvCustomInstructionGenerator.getFreeOpcode();
    private static final String mod_exec_opcode = RiscvCustomInstructionGenerator.getFreeOpcode();
    private static final String mod_mov_in_opcode = RiscvCustomInstructionGenerator.getFreeOpcode();
    private static final String mod_mov_out_opcode = RiscvCustomInstructionGenerator.getFreeOpcode();

    
    // All instruction should use U-type format (more flexible for this application)
    // Instead of using func parts of the instruction, only opcodes should probably be used (more usable bits)
    
    //module[20]module_index[5]opcode[7]
    public static String generateLoadModuleInstruction(String module, String module_index) {
        return module + module_index + RiscvCustomInstructionGenerator.mod_ld_opcode;
    }
    
    public static String generateExecuteModuleInstruction(String module_index) {
        return module_index + RiscvCustomInstructionGenerator.mod_exec_opcode;
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
