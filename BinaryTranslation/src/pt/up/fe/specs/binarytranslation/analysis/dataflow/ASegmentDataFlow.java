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

package pt.up.fe.specs.binarytranslation.analysis.dataflow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import pt.up.fe.specs.binarytranslation.analysis.AnalysisUtils;
import pt.up.fe.specs.binarytranslation.analysis.memory.AddressVertex;
import pt.up.fe.specs.binarytranslation.analysis.memory.AddressVertex.AddressVertexType;
import pt.up.fe.specs.binarytranslation.analysis.memory.transforms.TransformHexToDecimal;
import pt.up.fe.specs.binarytranslation.analysis.memory.transforms.TransformRemoveTemporaryVertices;
import pt.up.fe.specs.binarytranslation.analysis.memory.transforms.TransformShiftsToMult;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.operand.Operand;

public abstract class ASegmentDataFlow {
    protected Map<String, AddressVertex> vertexCache = new HashMap<String, AddressVertex>();
    protected Graph<AddressVertex, DefaultEdge> dfg = new DefaultDirectedGraph<>(DefaultEdge.class);
    protected List<Instruction> segment;

    public ASegmentDataFlow(List<Instruction> segment) {
        this.segment = segment;
    }

    public Graph<AddressVertex, DefaultEdge> buildDFG() {
        for (var i : segment) {
            //System.out.println(i.getRepresentation() + "  " + i.getData().getGenericTypes().toString());
            var op1 = i.getData().getOperands().get(0);
            var op2 = i.getData().getOperands().get(1);
            var op3 = i.getData().getOperands().size() == 3 ? i.getData().getOperands().get(2) : null;

            String rD = operandAsString(op1);
            String rA = operandAsString(op2);
            String rB = op3 == null ? "nop" : operandAsString(op3);

            var rDvert = new AddressVertex(rD, opType(op1));
            var rAvert = new AddressVertex(rA, opType(op2));
            var rBvert = op3 == null ? AddressVertex.nullVertex : new AddressVertex(rB, opType(op3));

            if (i.isMemory()) {
                handleMemoryInstruction(i, rDvert, rAvert, rBvert);
            }
            if (i.isJump()) {
                handleJumpInstruction(i, rDvert, rAvert, rBvert);
            } else {
                handleLogicalArithmeticInstruction(i, rDvert, rAvert, rBvert);
            }
        }

        applyTransforms();
        return dfg;
    }

    private void applyTransforms() {
        var t1 = new TransformHexToDecimal(dfg);
        var t2 = new TransformShiftsToMult(dfg);
        var t3 = new TransformRemoveTemporaryVertices(dfg);
        t1.applyToGraph();
        t2.applyToGraph();
        for (int i = 0; i < 4; i++)
            t3.applyToGraph();
    }

    private void handleJumpInstruction(Instruction i, AddressVertex rDvert, AddressVertex rAvert,
            AddressVertex rBvert) {
        if (rBvert == AddressVertex.nullVertex) {
            rAvert = addVertex(rAvert, false);
            rDvert = addVertex(rDvert, false);

            var jmpVertex = new AddressVertex(AnalysisUtils.mapInstructionsToSymbol("Jump"),
                    AddressVertexType.JUMP);
            dfg.addVertex(jmpVertex);
            dfg.addEdge(rAvert, jmpVertex);
            dfg.addEdge(rDvert, jmpVertex);

        } else {
            System.out.println("Not implemented");
        }
    }

    private void handleLogicalArithmeticInstruction(Instruction i, AddressVertex rDvert, AddressVertex rAvert,
            AddressVertex rBvert) {
        rAvert = addVertex(rAvert, false);
        rBvert = addVertex(rBvert, false);
        rDvert = addVertex(rDvert, true);

        var opSymbol = i.getName();
        var opVertex = new AddressVertex(AnalysisUtils.mapInstructionsToSymbol(opSymbol),
                AddressVertexType.OPERATION);
        dfg.addVertex(opVertex);
        dfg.addEdge(rAvert, opVertex);
        dfg.addEdge(rBvert, opVertex);
        dfg.addEdge(opVertex, rDvert);
    }

    private void handleMemoryInstruction(Instruction i, AddressVertex rDvert, AddressVertex rAvert,
            AddressVertex rBvert) {
        rAvert = addVertex(rAvert, false);
        rBvert = addVertex(rBvert, false);
        rDvert = addVertex(rDvert, i.isLoad());

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
        if (v.getType() == AddressVertexType.REGISTER) {
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
        } else {
            dfg.addVertex(v);
            return v;
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
}
