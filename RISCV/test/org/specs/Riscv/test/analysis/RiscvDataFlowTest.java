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

package org.specs.Riscv.test.analysis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.specs.Riscv.asm.RiscvELFDump;

import pt.up.fe.specs.binarytranslation.ELFProvider;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.utils.BinaryTranslationUtils;

public class RiscvDataFlowTest {
    public List<Instruction> getBasicBlock(ELFProvider elf, long start, long end) {
        var fd = BinaryTranslationUtils.getFile(elf.asTxtDump());
        var dump = new RiscvELFDump(fd);
        System.out.println("Start: " + start + ", end: " + end);
        var addr = new ArrayList<>(dump.getElfdump().keySet());
        Collections.sort(addr);
        for (var i : addr) {
            System.out.println(dump.getElfdump().get(i));
        }
        return dump.getRange(start, end);
    }
    
    @Test
    public void testScheduling() {
        var elfs = RiscvBasicBlockStaticInfo.getPolybenchSmallFloatKernels();
        for (var elf : elfs.keySet()) {
            System.out.println("ELF: " + elf.getFilename());
            
            for (var bound : elfs.get(elf)) {
                if (bound.length == 0) {
                    continue;
                }
                var start = bound[0];
                var end = bound[1];
                var bb = getBasicBlock(elf, start, end);
                
                for (var i : bb)
                    System.out.println(i);
                System.out.println("-------------------");
            }
        }
    }
}
