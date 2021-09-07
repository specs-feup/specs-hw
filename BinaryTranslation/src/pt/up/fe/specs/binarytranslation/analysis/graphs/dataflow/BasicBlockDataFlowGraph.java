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

package pt.up.fe.specs.binarytranslation.analysis.graphs.dataflow;

import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;

public class BasicBlockDataFlowGraph extends ASegmentDataFlowGraph {
    private static final long serialVersionUID = -4935282237892925083L;

    public BasicBlockDataFlowGraph(List<Instruction> segment, int repetitions) {
        super(getTransformedBasicBlock(segment, repetitions));
    }

    public BasicBlockDataFlowGraph(List<Instruction> segment) {
        this(segment, 1);
    }

    private static List<Instruction> getTransformedBasicBlock(List<Instruction> oldBB, int repetitions) {
        var<Instruction> newBB = new ArrayList<Instruction>();
        var size = oldBB.size();

        newBB.add(oldBB.get(size - 1));
        newBB.addAll(oldBB.subList(0, size - 1));

        var finalBB = new ArrayList<Instruction>();
        for (int i = 0; i < repetitions; i++) {
            finalBB.addAll(newBB);
        }

        System.out.println("Transformed BB:");
        var cnt = 0;
        for (var i : newBB) {
            System.out.println(i.getRepresentation());
            cnt += i.getLatency();
        }
        System.out.println("Total BB latency: " + cnt);
        System.out.println("-------------------");

        return finalBB;
    }
}
