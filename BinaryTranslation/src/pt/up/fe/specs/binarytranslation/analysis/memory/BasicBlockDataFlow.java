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

package pt.up.fe.specs.binarytranslation.analysis.memory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import pt.up.fe.specs.binarytranslation.analysis.AnalysisUtils;
import pt.up.fe.specs.binarytranslation.analysis.memory.AddressVertex.AddressVertexType;
import pt.up.fe.specs.binarytranslation.analysis.occurrence.BasicBlockOccurrenceTracker;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.operand.Operand;

public class BasicBlockDataFlow extends APropertyDetector {
    private Map<String, AddressVertex> vertexCache = new HashMap<String, AddressVertex>();
    private Graph<AddressVertex, DefaultEdge> dfg = new DefaultDirectedGraph<>(DefaultEdge.class);

    public BasicBlockDataFlow(BasicBlockOccurrenceTracker tracker) {
        super(tracker);
    }

    public Graph<AddressVertex, DefaultEdge> buildDFG() {
        var bb = getTransformedBasicBlock();

        for (var i : bb) {
            System.out.println(i.getRepresentation() + "  " + i.getData().getGenericTypes().toString());
            var op1 = i.getData().getOperands().get(0);
            var op2 = i.getData().getOperands().get(1);
            var op3 = i.getData().getOperands().size() == 3 ? i.getData().getOperands().get(2) : null;

            if (i.isMemory()) {
                handleMemoryInstruction(i, op1, op2, op3);
            }
            if (i.isAdd() || i.isMul() || i.isLogical() || i.isSub()) {
                handleLogicalArithmeticInstruction(i, op1, op2, op3);
            }
        }
        return dfg;
    }

    private void handleLogicalArithmeticInstruction(Instruction i, Operand op1, Operand op2, Operand op3) {
        String rD = operandAsString(op1);
        String rA = operandAsString(op2);
        String rB = operandAsString(op3);

        var rDvert = new AddressVertex(rD, opType(op1));
        var rAvert = new AddressVertex(rA, opType(op2));
        var rBvert = new AddressVertex(rB, opType(op3));
        rDvert = addVertex(rDvert, true);
        rAvert = addVertex(rAvert, false);
        rBvert = addVertex(rBvert, false);

        var opSymbol = i.getName();
        var opVertex = new AddressVertex(AnalysisUtils.mapInstructionsToSymbol(opSymbol),
                AddressVertexType.OPERATION);
        dfg.addVertex(opVertex);
        dfg.addEdge(rAvert, opVertex);
        dfg.addEdge(rBvert, opVertex);
        dfg.addEdge(opVertex, rDvert);
    }

    private void handleMemoryInstruction(Instruction i, Operand op1, Operand op2, Operand op3) {
        String rD = operandAsString(op1);
        String rA = operandAsString(op2);
        String rB = operandAsString(op3);

        var rDvert = new AddressVertex(rD, opType(op1));
        var rAvert = new AddressVertex(rA, opType(op2));
        var rBvert = new AddressVertex(rB, opType(op3));
        rDvert = addVertex(rDvert, i.isLoad());
        rAvert = addVertex(rAvert, false);
        rBvert = addVertex(rBvert, false);

        var memVert = new AddressVertex(i.isLoad() ? "Load" : "Store", AddressVertexType.MEMORY);
        dfg.addVertex(memVert);
        dfg.addEdge(rAvert, memVert);
        dfg.addEdge(rBvert, memVert);
        if (i.isLoad())
            dfg.addEdge(memVert, rDvert);
        else
            dfg.addEdge(rDvert, memVert);
    }

    private AddressVertex addVertex(AddressVertex v, boolean write) {
        if (write) {
            dfg.addVertex(v);
            vertexCache.put(v.getLabel(), v);
            return v;
        } else {
            if (vertexCache.containsKey(v.getLabel()))
                return vertexCache.get(v.getLabel());
            else {
                vertexCache.put(v.getLabel(), v);
                dfg.addVertex(v);
                return v;
            }
        }
    }

    private AddressVertexType opType(Operand op) {
        if (op.isImmediate())
            return AddressVertexType.IMMEDIATE;
        else
            return AddressVertexType.REGISTER;
    }

    private String operandAsString(Operand op) {
        if (op.isRegister()) {
            return AnalysisUtils.getRegName(op);
        }
        if (op.isImmediate())
            return op.getRepresentation();
        return "unknown";
    }

    private List<Instruction> getTransformedBasicBlock() {
        var newBB = new ArrayList<Instruction>();
        var oldBB = tracker.getBasicBlock().getInstructions();
        var size = oldBB.size();

        newBB.add(oldBB.get(size - 1));
        newBB.addAll(oldBB.subList(0, size - 2));
        return newBB;
    }
}
