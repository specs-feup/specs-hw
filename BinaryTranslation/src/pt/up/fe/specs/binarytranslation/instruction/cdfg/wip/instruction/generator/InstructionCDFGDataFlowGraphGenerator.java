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

import java.util.List;
import java.util.Map;

import org.jgrapht.graph.DefaultEdge;

import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.general.graph.DataFlowGraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.instruction.node.AInstructionCDFGNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.instruction.node.data.AInstructionCDFGDataNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.instruction.node.data.InstructionCDFGLiteralNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.instruction.node.data.InstructionCDFGVariableNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.instruction.node.operation.AInstructionCDFGOperationNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.instruction.node.operation.InstructionCDFGOperationNodeMap;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionBaseVisitor;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.AsmFieldContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.AssignmentExprContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.BinaryExprContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.IfElseStatementContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.IfStatementContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.LiteralContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.OperatorContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.UnaryExprContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.VariableExprContext;

public class InstructionCDFGDataFlowGraphGenerator extends PseudoInstructionBaseVisitor<AInstructionCDFGNode>{

    private DataFlowGraph<AInstructionCDFGNode, DefaultEdge> dfg;
    private Map<String, Integer> uid_map;
  

    private final static Class<AInstructionCDFGDataNode> DV = AInstructionCDFGDataNode.class;
    private final static Class<AInstructionCDFGOperationNode> OV = AInstructionCDFGOperationNode.class;
    private final static Class<DefaultEdge> E = DefaultEdge.class;
    
    private void setup(Map<String, Integer> uid_map){
        this.dfg = new DataFlowGraph<AInstructionCDFGNode, DefaultEdge>(DV,OV,E);
        this.uid_map = uid_map;
    }
    
    
    public Map<String, Integer> getUIDMap(){
        return this.uid_map;
    }
    
    public DataFlowGraph<AInstructionCDFGNode, DefaultEdge> generate(Map<String, Integer> uid_map, PseudoInstructionParser.ExpressionContext ctx){
        
        this.setup(uid_map);
        
        this.dfg.addOutput(this.visit(ctx));
        
        return this.dfg;
    }
    
    public DataFlowGraph<AInstructionCDFGNode, DefaultEdge> generate(Map<String, Integer> uid_map, AssignmentExprContext ctx){
        
        this.setup(uid_map);
        
        this.visit(ctx);

        return this.dfg;
    }
    
    
    public DataFlowGraph<AInstructionCDFGNode, DefaultEdge> generate(Map<String, Integer> uid_map,List<PseudoInstructionParser.StatementContext> ctx){
        
        this.setup(uid_map);
        
        ctx.forEach((statement) -> {this.visit(statement);});
        
        return this.dfg;
    }

    @Override
    public AInstructionCDFGNode visitAssignmentExpr(AssignmentExprContext ctx) {
        
        AInstructionCDFGNode output = this.visit(ctx.left);
        AInstructionCDFGNode operand =  this.visit(ctx.right);
        
        this.dfg.addOutput(output);
        
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
        InstructionCDFGGenerator.generateUID(this.uid_map, operand_left);
        InstructionCDFGGenerator.generateUID(this.uid_map, operand_right);
        
        return this.dfg.addOperation(operator, operand_left , operand_right);
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
        return new InstructionCDFGLiteralNode(ctx.getText());
    }
    
    @Override
    public AInstructionCDFGNode visitVariableExpr(VariableExprContext ctx) {
        return new InstructionCDFGVariableNode(ctx.getText());
    }
    
    @Override
    public AInstructionCDFGNode visitAsmField(AsmFieldContext ctx) {
        return new InstructionCDFGVariableNode(ctx.getText());
    }
}
