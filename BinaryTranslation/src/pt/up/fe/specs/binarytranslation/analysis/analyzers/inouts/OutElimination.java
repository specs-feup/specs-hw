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
 
package pt.up.fe.specs.binarytranslation.analysis.analyzers.inouts;

import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.binarytranslation.analysis.analyzers.ocurrence.BasicBlockOccurrenceTracker;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;

@Deprecated
public class OutElimination {

    private List<Instruction> insts;

    public OutElimination(BinarySegment bb, List<Instruction> insts) {
        this.insts = insts;
    }

    public void eliminate(int windowSize) {
        var occur = new BasicBlockOccurrenceTracker(null, insts);
        var sbbio = new SimpleBasicBlockInOuts(null);
        sbbio.calculateInOuts();
        var inouts = sbbio.getInouts();

        for (var o : occur.getOccurrences()) {
            var elim = new OutEliminationOccurrence(o, occur, insts, inouts);
            var elimList = elim.eliminate(windowSize);
            printOccurrenceResult(elimList, windowSize);
        }
    }
    
    private void printOccurrenceResult(ArrayList<String> regs, int window) {
        var sb = new StringBuilder();
        sb.append(String.join(",", regs));
        sb.append("; window=").append(window);
        System.out.println(sb.toString());
    }
}
