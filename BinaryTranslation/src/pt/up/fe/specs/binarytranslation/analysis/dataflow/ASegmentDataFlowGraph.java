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

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import pt.up.fe.specs.binarytranslation.analysis.AnalysisUtils;
import pt.up.fe.specs.binarytranslation.analysis.dataflow.DataFlowVertex.DataFlowVertexType;
import pt.up.fe.specs.binarytranslation.analysis.graphs.transforms.TransformHexToDecimal;
import pt.up.fe.specs.binarytranslation.analysis.graphs.transforms.TransformRemoveTemporaryVertices;
import pt.up.fe.specs.binarytranslation.analysis.graphs.transforms.TransformShiftsToMult;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.operand.Operand;

public abstract class ASegmentDataFlowGraph extends SimpleDirectedGraph<DataFlowVertex, DefaultEdge> {
    private static final long serialVersionUID = 4454283993649695154L;
    protected Map<String, DataFlowVertex> vertexCache = new HashMap<String, DataFlowVertex>();
    protected List<Instruction> segment;

    public ASegmentDataFlowGraph(List<Instruction> segment) {
        super(DefaultEdge.class);
        this.segment = segment;
        buildDFG();
    }

    private void buildDFG() {
        for (var i : segment) {
            var op1 = i.getData().getOperands().get(0);
            var op2 = i.getData().getOperands().get(1);
            var op3 = i.getData().getOperands().size() == 3 ? i.getData().getOperands().get(2) : null;

            String rD = operandAsString(op1);
            String rA = operandAsString(op2);
            String rB = op3 == null ? "nop" : operandAsString(op3);

            var rDvert = new DataFlowVertex(rD, opType(op1));
            var rAvert = new DataFlowVertex(rA, opType(op2));
            var rBvert = op3 == null ? DataFlowVertex.nullVertex : new DataFlowVertex(rB, opType(op3));

            if (i.isMemory()) {
                handleMemoryInstruction(i, rDvert, rAvert, rBvert);
            } else if (i.isJump()) {
                handleJumpInstruction(i, rDvert, rAvert, rBvert);
            } else {
                handleLogicalArithmeticInstruction(i, rDvert, rAvert, rBvert);
            }
        }

        applyTransforms();
    }

    private void applyTransforms() {
        var t1 = new TransformHexToDecimal(this);
        var t2 = new TransformShiftsToMult(this);
        var t3 = new TransformRemoveTemporaryVertices(this);
        t1.applyToGraph();
        t2.applyToGraph();
        for (int i = 0; i < 4; i++)
            t3.applyToGraph();
    }

    private void handleJumpInstruction(Instruction i, DataFlowVertex rDvert, DataFlowVertex rAvert,
            DataFlowVertex rBvert) {
        if (rBvert == DataFlowVertex.nullVertex) {
            rAvert = addVertex(rAvert, false);
            rDvert = addVertex(rDvert, false);

            var jmpVertex = new DataFlowVertex(AnalysisUtils.mapInstructionsToSymbol("Jump"),
                    DataFlowVertexType.JUMP);
            addVertex(jmpVertex);
            addEdge(rAvert, jmpVertex);
            addEdge(rDvert, jmpVertex);

        } else {
            System.out.println("Not implemented");
        }
    }

    private void handleLogicalArithmeticInstruction(Instruction i, DataFlowVertex rDvert, DataFlowVertex rAvert,
            DataFlowVertex rBvert) {
        rAvert = addVertex(rAvert, false);
        rBvert = addVertex(rBvert, false);
        rDvert = addVertex(rDvert, true);

        var opSymbol = i.getName();
        var opVertex = new DataFlowVertex(AnalysisUtils.mapInstructionsToSymbol(opSymbol),
                DataFlowVertexType.OPERATION);
        addVertex(opVertex);
        addEdge(rAvert, opVertex);
        addEdge(rBvert, opVertex);
        addEdge(opVertex, rDvert);
    }

    private void handleMemoryInstruction(Instruction i, DataFlowVertex rDvert, DataFlowVertex rAvert,
            DataFlowVertex rBvert) {
        rAvert = addVertex(rAvert, false);
        rBvert = addVertex(rBvert, false);
        rDvert = addVertex(rDvert, i.isLoad());

        var memVert = new DataFlowVertex(i.isLoad() ? "Load" : "Store", DataFlowVertexType.MEMORY);
        addVertex(memVert);
        addEdge(rAvert, memVert);
        addEdge(rBvert, memVert);
        if (i.isLoad())
            addEdge(memVert, rDvert);
        else
            addEdge(rDvert, memVert);
    }

    private DataFlowVertex addVertex(DataFlowVertex v, boolean write) {
        if (v.getType() == DataFlowVertexType.REGISTER) {
            if (write) {
                addVertex(v);
                vertexCache.put(v.getLabel(), v);
                return v;
            } else {
                if (vertexCache.containsKey(v.getLabel()))
                    return vertexCache.get(v.getLabel());
                else {
                    vertexCache.put(v.getLabel(), v);
                    addVertex(v);
                    return v;
                }
            }
        } else {
            addVertex(v);
            return v;
        }
    }

    private DataFlowVertexType opType(Operand op) {
        if (op.isImmediate())
            return DataFlowVertexType.IMMEDIATE;
        else
            return DataFlowVertexType.REGISTER;
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
