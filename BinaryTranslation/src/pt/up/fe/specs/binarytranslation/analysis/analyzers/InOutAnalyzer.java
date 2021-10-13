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
 
package pt.up.fe.specs.binarytranslation.analysis.analyzers;

import java.util.List;

import pt.up.fe.specs.binarytranslation.ZippedELFProvider;
import pt.up.fe.specs.binarytranslation.analysis.AnalysisUtils;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.inouts.BasicBlockInOuts;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.inouts.OutElimination;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.inouts.SimpleBasicBlockInOuts;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.inouts.TraceInOuts;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;

public class InOutAnalyzer extends ABasicBlockAnalyzer {
    public enum InOutMode {
        BASIC_BLOCK,
        SIMPLE_BASIC_BLOCK,
        TRACE,
        ELIMINATION
    };

    public InOutAnalyzer(ATraceInstructionStream stream, ZippedELFProvider elf, int window) {
        super(stream, elf, window);
    }
    
    public void analyse(InOutMode mode) {
        var bbs = this.getBasicBlocks();
        
        if (mode == InOutMode.BASIC_BLOCK) {
            for (var bb : bbs) {
                BasicBlockInOuts inouts = new BasicBlockInOuts(bb);
                inouts.calculateInOuts();

                inouts.printUseDefRegisters();
                inouts.printSequenceInOuts();
                inouts.printResult();
            }
        }
        if (mode == InOutMode.SIMPLE_BASIC_BLOCK) {
            for (var bb : bbs) {
                SimpleBasicBlockInOuts inouts = new SimpleBasicBlockInOuts(bb);
                inouts.calculateInOuts();
                inouts.printResult();
                
            }
        }
        if (mode == InOutMode.ELIMINATION) {
            for (var bb : bbs) {
                System.out.println("Running elimination analysis for detected Basic Block");
                System.out.println("");
//                var elim = new OutElimination(bb, det.getProcessedInsts());
//                elim.eliminate(15);
            }
        }
    }
}
