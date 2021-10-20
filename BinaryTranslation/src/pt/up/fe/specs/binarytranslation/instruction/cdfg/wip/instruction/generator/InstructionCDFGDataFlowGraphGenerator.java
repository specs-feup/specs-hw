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

package pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.instruction.generator;

import java.util.*;

import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.general.graph.DataFlowGraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.instruction.edge.AInstructionCDFGEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.instruction.edge.operand.InstructionCDFGLeftOperandEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.instruction.edge.operand.InstructionCDFGRightOperandEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.instruction.modifier.*;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.instruction.node.AInstructionCDFGNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.instruction.node.data.*;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.instruction.node.operation.AInstructionCDFGOperationNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.instruction.node.operation.InstructionCDFGOperationNodeMap;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionBaseVisitor;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.*;

public class InstructionCDFGDataFlowGraphGenerator extends PseudoInstructionBaseVisitor<AInstructionCDFGNode>{

    private DataFlowGraph<AInstructionCDFGNode, AInstructionCDFGEdge> dfg;
    private Map<String, Integer> uid_map;
   
    private AInstructionCDFGSubscriptModifier subscript = null;

    private final static Class<AInstructionCDFGDataNode> DV = AInstructionCDFGDataNode.class;
    private final static Class<AInstructionCDFGOperationNode> OV = AInstructionCDFGOperationNode.class;
    private final static Class<AInstructionCDFGEdge> E = AInstructionCDFGEdge.class;
    
    private void setup(Map<String, Integer> uid_map){
        this.dfg = new DataFlowGraph<AInstructionCDFGNode, AInstructionCDFGEdge>(DV,OV,E);
        this.uid_map = uid_map;
    }
    
    
    public Map<String, Integer> getUIDMap(){
        return this.uid_map;
    }
    
    public DataFlowGraph<AInstructionCDFGNode, AInstructionCDFGEdge> generate(Map<String, Integer> uid_map, PseudoInstructionParser.ExpressionContext ctx){
        
        this.setup(uid_map);
        
        this.dfg.addOutput(this.visit(ctx));
        
        return this.dfg;
    }

    public DataFlowGraph<AInstructionCDFGNode, AInstructionCDFGEdge> generate(Map<String, Integer> uid_map, PseudoInstructionParser.PlainStmtContext ctx){
        
        this.setup(uid_map);
        
        this.visit(ctx.expression());

        return this.dfg;
    }
    
    public DataFlowGraph<AInstructionCDFGNode, AInstructionCDFGEdge> generate(Map<String, Integer> uid_map, AssignmentExprContext ctx){
        
        this.setup(uid_map);
        
        this.visit(ctx);

        return this.dfg;
    }
    
    public DataFlowGraph<AInstructionCDFGNode, AInstructionCDFGEdge> generate(Map<String, Integer> uid_map,List<PseudoInstructionParser.StatementContext> ctx){
        
        this.setup(uid_map);
        
        ctx.forEach((statement) -> {this.visit(statement);});
        
        return this.dfg;
    }

    @Override
    public AInstructionCDFGNode visitAssignmentExpr(AssignmentExprContext ctx) {
        
        AInstructionCDFGNode output = this.dfg.addOutput(this.visit(ctx.left));
        AInstructionCDFGNode operand =  this.visit(ctx.right);
        
        InstructionCDFGGenerator.generateUID(this.uid_map, output);
        InstructionCDFGGenerator.generateUID(this.uid_map, operand);
        
        return this.dfg.addOperation(output, this.dfg.addInput(operand));
    }
    
    @Override
    public AInstructionCDFGNode visitBinaryExpr(BinaryExprContext ctx) { 
        
        AInstructionCDFGNode operator = this.visit(ctx.operator());
        AInstructionCDFGNode operand_left = this.dfg.addInput(this.visit(ctx.left));
        AInstructionCDFGNode operand_right = this.dfg.addInput(this.visit(ctx.right));
        
        InstructionCDFGGenerator.generateUID(this.uid_map, operator);
        this.dfg.addVertex(operator);
        
        InstructionCDFGGenerator.generateUID(this.uid_map, operand_left);
        
        if(this.subscript == null) {
            this.dfg.addEdge(operand_left, operator, new InstructionCDFGLeftOperandEdge());
        }else {
            this.dfg.addEdge(operand_left, operator, new InstructionCDFGLeftOperandEdge(this.subscript));
            this.subscript = null;
        }
        
        InstructionCDFGGenerator.generateUID(this.uid_map, operand_right);
        
        if(this.subscript == null) {
            this.dfg.addEdge(operand_right, operator, new InstructionCDFGRightOperandEdge());
        }else {
            this.dfg.addEdge(operand_right, operator, new InstructionCDFGRightOperandEdge(this.subscript));
            this.subscript = null;
        }
        
        return operator;
    }
    
    @Override
    public AInstructionCDFGNode visitUnaryExpr(UnaryExprContext ctx) {
        
        AInstructionCDFGNode operator = this.visit(ctx.operator());
        AInstructionCDFGNode operand = this.dfg.addInput(this.visit(ctx.right));
       
        InstructionCDFGGenerator.generateUID(this.uid_map, operator);
        InstructionCDFGGenerator.generateUID(this.uid_map, operand);
        
        return this.dfg.addOperation(operator, operand);
    }
    
    @Override
    public AInstructionCDFGNode visitIfElseStatement(IfElseStatementContext ctx) {
        return null;
    }
    
    @Override
    public AInstructionCDFGNode visitIfStatement(IfStatementContext ctx) {
        return null;
    }
    
    @Override
    public AInstructionCDFGNode visitOperator(OperatorContext ctx) {
        return InstructionCDFGOperationNodeMap.generate(ctx.getText());
    }
    
    @Override
    public AInstructionCDFGNode visitLiteral(LiteralContext ctx) {
        return new InstructionCDFGLiteralNode(ctx.number().getText());
    }
    
   /* @Override
    public AInstructionCDFGNode visitVariableExpr(VariableExprContext ctx) {
        return new InstructionCDFGLiteralNode(ctx.getText());
    }*/
    
    @Override
    public AInstructionCDFGNode visitField(FieldContext ctx) {
        return new InstructionCDFGVariableNode(ctx.getText());
    }
    
    @Override
    public AInstructionCDFGNode visitRangesubscript(RangesubscriptContext ctx) {
        
        this.subscript = new InstructionCDFGRangeSubscript(Integer.valueOf(ctx.hiidx.getText()), Integer.valueOf(ctx.loidx.getText()));
        return new InstructionCDFGVariableNode(ctx.getText().replace("[", "u").replace(":","l").replace("]", ""));
    }
    @Override
    public AInstructionCDFGNode visitScalarsubscript(ScalarsubscriptContext ctx) {
        
        this.subscript = new InstructionCDFGScalarSubscript(Integer.valueOf(ctx.idx.getText()));

        return new InstructionCDFGVariableNode(ctx.getText().replace("[", "u").replace("]", " "));
    }
    
    @Override
    public AInstructionCDFGNode visitMetafield(MetafieldContext ctx) {
        return new InstructionCDFGVariableNode(ctx.getText());
    }
 
}
