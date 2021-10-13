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

import java.util.List;

import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;

public class TraceInOuts extends ASequenceInOuts {

    private List<BinarySegment> bbs;

    public TraceInOuts(List<Instruction> insts, List<BinarySegment> bbs) {
        super(insts);
        this.bbs = bbs;
    }

    @Override
    public void printSequenceInOuts() {
        for (BinarySegment bb : bbs) {
            long start = bb.getInstructions().get(0).getAddress();
            long end = bb.getInstructions().get(bb.getInstructions().size() - 1).getAddress();
            boolean expectingEnd = false;
            
            System.out.println("Displaying in/outs for all occurrences of basic block");
            System.out.println("-----------------------------------------------------");
            for (int i = 0; i < insts.size(); i++) {
                Instruction inst = insts.get(i);
                if (inst.getAddress() == start && !expectingEnd) {
                    printInstWithSets(i);
                    System.out.println("[basic block body...]");
                    expectingEnd = true;
                }
                if (inst.getAddress() == end && expectingEnd) {
                    printInstWithSets(i);
                    expectingEnd = false;
                    System.out.println("-----------------------------------------------------");
                }
            }
        }
    }

    @Override
    public void printResult() {
        // TODO Auto-generated method stub

    }

}
