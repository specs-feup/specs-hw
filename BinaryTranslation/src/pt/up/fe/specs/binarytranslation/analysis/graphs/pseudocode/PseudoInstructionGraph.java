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

package pt.up.fe.specs.binarytranslation.analysis.graphs.pseudocode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex;
import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex.BtfVertexType;
import pt.up.fe.specs.binarytranslation.analysis.graphs.GraphUtils;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.ast.InstructionAST;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.OperatorASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.PseudoInstructionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.expr.AssignmentExpressionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.expr.BinaryExpressionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.expr.FunctionExpressionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.expr.UnaryExpressionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.operand.BareOperandASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.operand.LiteralOperandASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.operand.ScalarSubscriptOperandASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.statement.PlainStatementASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.transformed.ImmediateOperandASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.transformed.VariableOperandASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.passes.ApplyInstructionPass;

public class PseudoInstructionGraph extends SimpleDirectedGraph<BtfVertex, DefaultEdge> {
    private static final long serialVersionUID = 415969460681065176L;
    private InstructionAST ast;
    private List<BtfVertex> inputs = new ArrayList<>();
    private List<BtfVertex> outputs = new ArrayList<>();
    private Instruction inst;

    public PseudoInstructionGraph(Instruction i) {
        super(DefaultEdge.class);
        
        this.inst = i;
        this.ast = new InstructionAST(i);
        ast.accept(new ApplyInstructionPass());
        System.out.println(ast.toString());

        buildGraph(ast.getRootnode());
        setInOuts();
        if (ast.getRootnode().getChildren().size() > 1)
            joinGraphs();
    }

    private void setInOuts() {
        for (var v : this.vertexSet()) {
            if (this.inDegreeOf(v) == 0)
                inputs.add(v);
            if (this.outDegreeOf(v) == 0)
                outputs.add(v);
        }
    }

    private BtfVertex buildGraph(InstructionASTNode currNode) {
        if (currNode instanceof PseudoInstructionASTNode) {
            for (var child : currNode.getChildren())
                buildGraph(child);
        }
        if (currNode instanceof PlainStatementASTNode) {
            buildGraph(currNode.getChildren().get(0));
        }
        if (currNode instanceof AssignmentExpressionASTNode) {
            handleAssignmentExpr(currNode);
        }
        if (currNode instanceof VariableOperandASTNode) {
            var vertex = new BtfVertex(currNode.getAsString(), BtfVertexType.REGISTER);
            this.addVertex(vertex);
            return vertex;
        }
        if (currNode instanceof BinaryExpressionASTNode) {
            return handleBinaryExpr(currNode);
        }
        if (currNode instanceof UnaryExpressionASTNode) {
            return handleUnaryExpr(currNode);
        }
        if (currNode instanceof OperatorASTNode) {
            var opVertex = new BtfVertex(currNode.getAsString(), BtfVertexType.OPERATION);
            this.addVertex(opVertex);
            return opVertex;
        }
        if ((currNode instanceof ImmediateOperandASTNode) || (currNode instanceof LiteralOperandASTNode)) {
            var immVertex = new BtfVertex(currNode.getAsString(), BtfVertexType.IMMEDIATE);
            this.addVertex(immVertex);
            return immVertex;
        }
        if (currNode instanceof ScalarSubscriptOperandASTNode) {
            return handleScalarSubscript(currNode);
        }
        if (currNode instanceof FunctionExpressionASTNode) {
            return handleFunctionExpr(currNode);
        }

        return BtfVertex.nullVertex;
    }

    private void joinGraphs() {
        // TODO Auto-generated method stub
        
    }

    private BtfVertex handleScalarSubscript(InstructionASTNode currNode) {
        var opVertex = new BtfVertex(currNode.getAsString(), BtfVertexType.OPERATION);
        this.addVertex(opVertex);
        
        var leftVertex = buildGraph(currNode.getChild(0));
        this.addEdge(opVertex, leftVertex);
        
        return opVertex;
    }

    private BtfVertex handleFunctionExpr(InstructionASTNode currNode) {
        var funName = currNode.getAsString().subSequence(0, currNode.getAsString().indexOf("("));
        funName = funName + "()";
        var funcVertex = new BtfVertex(funName.toString(), BtfVertexType.OPERATION);
        this.addVertex(funcVertex);
        
        var argVertex = buildGraph(currNode.getChild(0));
        this.addEdge(argVertex, funcVertex);
        
        return funcVertex;
    }

    private BtfVertex handleUnaryExpr(InstructionASTNode currNode) {
        var op = currNode.getChildren().get(0);
        var right = currNode.getChildren().get(1);
    
        var opVertex = buildGraph(op);
        var rightVertex = buildGraph(right);
        
        this.addEdge(rightVertex, opVertex);
        return opVertex;
    }

    private BtfVertex handleBinaryExpr(InstructionASTNode currNode) {
        var left = currNode.getChildren().get(0);
        var op = currNode.getChildren().get(1);
        var right = currNode.getChildren().get(2);

        var leftVertex = buildGraph(left);
        var opVertex = buildGraph(op);
        var rightVertex = buildGraph(right);
        
        this.addEdge(leftVertex, opVertex);
        this.addEdge(rightVertex, opVertex);
        return opVertex;
    }

    private void handleAssignmentExpr(InstructionASTNode currNode) {
        var left = currNode.getChildren().get(0);
        var right = currNode.getChildren().get(1);

        var leftVertex = buildGraph(left);
        var rightVertex = buildGraph(right);
        this.addEdge(rightVertex, leftVertex);
    }

    public List<BtfVertex> getInputs() {
        return inputs;
    }

    public List<BtfVertex> getOutputs() {
        return outputs;
    }

    @Override
    public String toString() {
        return GraphUtils.generateGraphURL(this);
    }

    public Instruction getOriginalInstruction() {
        return inst;
    }
}
