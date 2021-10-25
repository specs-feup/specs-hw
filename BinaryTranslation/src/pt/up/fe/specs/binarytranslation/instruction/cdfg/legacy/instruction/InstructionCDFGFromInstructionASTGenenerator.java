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

package pt.up.fe.specs.binarytranslation.instruction.cdfg.legacy.instruction;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.ast.InstructionAST;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.OperatorASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.PseudoInstructionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.expr.AssignmentExpressionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.expr.BinaryExpressionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.expr.ExpressionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.expr.FunctionExpressionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.expr.UnaryExpressionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.operand.BareOperandASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.operand.LiteralOperandASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.operand.MetaOperandASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.operand.OperandASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.operand.RangeSubscriptOperandASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.operand.ScalarSubscriptOperandASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.statement.IfElseStatementASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.statement.IfStatementASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.statement.PlainStatementASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.statement.StatementASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.transformed.ConcreteOperandASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.transformed.ImmediateOperandASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.transformed.VariableOperandASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.passes.InstructionASTVisitor;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.legacy.generic.node.AGenericCDFGNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.legacy.generic.node.GenericCDFGNodeGenerator;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.legacy.generic.node.data.AGenericCDFGDataNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.legacy.generic.node.data.GenericCDFGLiteralNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.legacy.generic.node.data.GenericCDFGVariableNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.legacy.generic.node.operation.expression.arithmetic.GenericCDFGAdditionNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.legacy.generic.node.operation.expression.arithmetic.GenericCDFGDivisionNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.legacy.generic.node.operation.expression.arithmetic.GenericCDFGMultiplicationNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.legacy.generic.node.operation.expression.arithmetic.GenericCDFGShiftLeftLogicalNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.legacy.generic.node.operation.expression.arithmetic.GenericCDFGShiftRightArithmeticNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.legacy.generic.node.operation.expression.arithmetic.GenericCDFGShiftRightLogicalNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.legacy.generic.node.operation.expression.arithmetic.GenericCDFGSubtractionNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.legacy.generic.node.operation.expression.bitwise.GenericCDFGBitwiseAndNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.legacy.generic.node.operation.expression.bitwise.GenericCDFGBitwiseNotNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.legacy.generic.node.operation.expression.bitwise.GenericCDFGBitwiseOrNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.legacy.generic.node.operation.expression.bitwise.GenericCDFGBitwiseXorNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.legacy.generic.node.operation.expression.comparison.GenericCDFGEqualsToNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.legacy.generic.node.operation.expression.comparison.GenericCDFGGreaterThanNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.legacy.generic.node.operation.expression.comparison.GenericCDFGGreaterThanOrEqualsToNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.legacy.generic.node.operation.expression.comparison.GenericCDFGLessThanNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.legacy.generic.node.operation.expression.comparison.GenericCDFGLessThanOrEqualsToNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.legacy.generic.node.operation.expression.comparison.GenericCDFGNotEqualsToNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.legacy.generic.node.operation.expression.logical.GenericCDFGLogicalAndNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.legacy.generic.node.operation.expression.logical.GenericCDFGLogicalNotNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.legacy.generic.node.operation.expression.logical.GenericCDFGLogicalOrNode;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.FunctionExprContext;

/**
 * @author João Conceição
 */

public class InstructionCDFGFromInstructionASTGenenerator extends InstructionASTVisitor<AGenericCDFGNode>{

    private InstructionCDFG icdfg;
    
   /*
    public AGenericCDFGNode visit(InstructionAST ast) throws Exception {
        
        this.visit(ast.getRootnode());
          
      return null;
    };
    
    public AGenericCDFGNode visit(PseudoInstructionASTNode node) {
        
        for(var statement : node.){
            this.visit(statement);
          } 
          
      return null;
    }
    */
    /*
    @Override
    public AGenericCDFGNode visit(StatementASTNode node) {
        return null;
    }
    */
    
    public InstructionCDFG generate(InstructionAST iast) throws Exception {
        
        this.icdfg = new InstructionCDFG();

        this.visit(iast);
     
        return this.icdfg;
    }
    
    @Override
    public AGenericCDFGNode visit(PlainStatementASTNode node) throws Exception {
        
        return this.visit(node.getExpr());
    }
    
    @Override
    public AGenericCDFGNode visit(IfStatementASTNode node) throws Exception {
        AGenericCDFGNode condition_node = this.visit(node.getCondition());
        
        for(var if_statement : node.getStatements().getChildren()) {
            this.icdfg.addEdge(condition_node, this.visit(if_statement), true);
        }
        
        return condition_node;
    }
    
    @Override
    public AGenericCDFGNode visit(IfElseStatementASTNode node) throws Exception{
        AGenericCDFGNode condition_node = this.visit(node.getCondition());
        
        for(var if_statement : node.getStatements().getChildren()) {
            this.icdfg.addEdge(condition_node, this.visit(if_statement), true);
        }
        
        for(var else_statement : node.getElseStatements().getChildren()) {
            this.icdfg.addEdge(condition_node, this.visit(else_statement), false);
        }
        
        return condition_node;
    }
    
    
    @Override
    public AGenericCDFGNode visit(ExpressionASTNode node) throws Exception{
        return super.visit(node);
    }
    
    @Override
    public AGenericCDFGNode visit(AssignmentExpressionASTNode node) throws Exception {
        
        AGenericCDFGNode output_node = this.visit(node.getTarget());
        AGenericCDFGNode expression_node = this.visit(node.getExpr());

        if(output_node instanceof AGenericCDFGDataNode) {
            output_node = this.icdfg.addOutputNode((AGenericCDFGDataNode)output_node);
        }
        
        if(expression_node instanceof AGenericCDFGDataNode) {
            expression_node = this.icdfg.addInputNode((AGenericCDFGDataNode)expression_node);
        }
        
        if(!this.icdfg.containsVertex(output_node)) {
            this.icdfg.addNode(output_node);
        }
        
        if(!this.icdfg.containsVertex(expression_node)) {
            this.icdfg.addNode(expression_node);
        }
        /*
        if(node.getTarget() instanceof FunctionExpressionASTNode) {
            this.icdfg.addEdge(expression_node, output_node,((FunctionExpressionASTNode)).functionName().getText() + "()");
        }else {
            this.icdfg.addEdge(expression_node, output_node);
        }*/
        
        this.icdfg.addEdge(expression_node, output_node);
        return expression_node;
    }
    
    @Override
    public AGenericCDFGNode visit(BinaryExpressionASTNode node) throws Exception{
        
        AGenericCDFGNode operator_node = this.icdfg.addNode(this.visit(node.getOperator()));
        AGenericCDFGNode expression_node_left = this.visit(node.getLeft());
        AGenericCDFGNode expression_node_right = this.visit(node.getRight());

      if(expression_node_left instanceof AGenericCDFGDataNode) {
          expression_node_left = this.icdfg.addInputNode((AGenericCDFGDataNode)expression_node_left);
      }
      
      if(expression_node_right instanceof AGenericCDFGDataNode) {
          expression_node_right = this.icdfg.addInputNode((AGenericCDFGDataNode)expression_node_right);
      }
      
      if(!this.icdfg.containsVertex(expression_node_left)) {
          this.icdfg.addNode(expression_node_left);
      }
      
      if(!this.icdfg.containsVertex(expression_node_right)) {
          this.icdfg.addNode(expression_node_right);
      }
      
      this.icdfg.addEdge(expression_node_left, operator_node);
      this.icdfg.addEdge(expression_node_right, operator_node);
      
      return operator_node; 
    }
    
    @Override
    public AGenericCDFGNode visit(UnaryExpressionASTNode node) throws Exception{

        AGenericCDFGNode operator_node = this.icdfg.addNode(this.visit(node.getOperator()));    
        AGenericCDFGNode expression_node = this.visit(node.getRight());

        if(expression_node instanceof AGenericCDFGDataNode) {
            expression_node = this.icdfg.addInputNode((AGenericCDFGDataNode)expression_node);
        }

        if(!this.icdfg.containsVertex(expression_node)) {
            this.icdfg.addNode(expression_node);
        }
        
        this.icdfg.addEdge(expression_node, operator_node);
        
        return operator_node;    
    }
    
    @Override
    public AGenericCDFGNode visit(FunctionExpressionASTNode node) throws Exception{
        return this.visit(node.getArgument(0));
    }
    
    /*
    @Override
    public AGenericCDFGNode visit(OperandASTNode node){
        return null;
    }
    */
    
    /*
    @Override
    public AGenericCDFGNode visit(ConcreteOperandASTNode node){
        return null;
    }
    */
    @Override
    public AGenericCDFGNode visit(BareOperandASTNode node){
        return new GenericCDFGVariableNode(node.getAsString());
    }
    
    
    @Override
    public AGenericCDFGNode visit(LiteralOperandASTNode node){
        return new GenericCDFGLiteralNode(node.getAsString());
    }
    
    @Override
    public AGenericCDFGNode visit(MetaOperandASTNode node){
        return new GenericCDFGVariableNode(node.getAsString());
    }
    
    @Override
    public AGenericCDFGNode visit(VariableOperandASTNode node){
        return new GenericCDFGVariableNode(node.getAsString());
    }
    
    @Override
    public AGenericCDFGNode visit(ImmediateOperandASTNode node){
        return new GenericCDFGLiteralNode(node.getAsString());
    }
    
    @Override
    public AGenericCDFGNode visit(ScalarSubscriptOperandASTNode node){
        return null;
    }
    
    @Override
    public AGenericCDFGNode visit(RangeSubscriptOperandASTNode node){
        return null;
    }
    
    @Override
    public AGenericCDFGNode visit(OperatorASTNode node){
        
        return InstructionCDFGGeneratorMaps.OPERATION_MAP.get(node.getAsString()).get().apply();
    }
}
