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

package pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.node.AGenericCDFGNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.node.GenericCDFGNodeGenerator;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.node.data.AGenericCDFGDataNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.node.data.GenericCDFGLiteralNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.node.data.GenericCDFGVariableNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.node.operation.expression.arithmetic.GenericCDFGAdditionNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.node.operation.expression.arithmetic.GenericCDFGDivisionNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.node.operation.expression.arithmetic.GenericCDFGMultiplicationNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.node.operation.expression.arithmetic.GenericCDFGShiftLeftLogicalNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.node.operation.expression.arithmetic.GenericCDFGShiftRightArithmeticNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.node.operation.expression.arithmetic.GenericCDFGShiftRightLogicalNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.node.operation.expression.arithmetic.GenericCDFGSubtractionNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.node.operation.expression.bitwise.GenericCDFGAssignNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.node.operation.expression.bitwise.GenericCDFGBitwiseAndNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.node.operation.expression.bitwise.GenericCDFGBitwiseNotNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.node.operation.expression.bitwise.GenericCDFGBitwiseOrNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.node.operation.expression.bitwise.GenericCDFGBitwiseXorNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.node.operation.expression.comparison.GenericCDFGEqualsToNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.node.operation.expression.comparison.GenericCDFGGreaterThanNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.node.operation.expression.comparison.GenericCDFGGreaterThanOrEqualsToNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.node.operation.expression.comparison.GenericCDFGLessThanNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.node.operation.expression.comparison.GenericCDFGLessThanOrEqualsToNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.node.operation.expression.comparison.GenericCDFGNotEqualsToNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.node.operation.expression.logical.GenericCDFGLogicalAndNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.node.operation.expression.logical.GenericCDFGLogicalNotNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.node.operation.expression.logical.GenericCDFGLogicalOrNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.generic.node.operation.function.GenericCDFGSignExtendNode;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionBaseVisitor;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.ArgumentsContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.AsmFieldContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.AssignmentExprContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.BinaryExprContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.FunctionExprContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.IfElseStatementContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.IfStatementContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.LiteralContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.MetaFieldContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.OperatorContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.ParenExprContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.PlainStmtContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.PseudoInstructionContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.UnaryExprContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.VariableExprContext;

/**
 * @author João Conceição
 */

public class InstructionCDFGGenerator extends PseudoInstructionBaseVisitor<AGenericCDFGNode>{

    private InstructionCDFG icdfg;
    
   
    /** Method for generating an InstructionCDFG from the ParseTree of an Instruction
     * 
     * @param inst
     * @return The InstructionCDFG corresponding to the passed Instruction
     */
    public InstructionCDFG generate(Instruction inst) {
           
        this.icdfg = new InstructionCDFG();

        this.visitPseudoInstructionAST(inst.getPseudocode().getParseTree());
     
        return this.icdfg;
    }
    
    public InstructionCDFG generate(PseudoInstructionContext inst) {
        
        this.icdfg = new InstructionCDFG();

        this.visitPseudoInstructionAST(inst);
     
        return this.icdfg;
    }
    
 // Top level //////////////////////////////////////////////////////////////
    public InstructionCDFG visitPseudoInstructionAST(PseudoInstructionContext ctx) {
        
        for(var statement : ctx.statement()){
          this.visit(statement);
        } 
        
        return null;
    }

    // Statement level ////////////////////////////////////////////////////////
   /* @Override
    public InstructionCDFG visitStatement(StatementContext ctx) {
        
        if(ctx instanceof PlainStmtContext)
            return visitPlainStmt((PlainStmtContext) ctx);
        else
            return null;
    }
    */
    
      @Override
     public AGenericCDFGNode visitPlainStmt(PlainStmtContext ctx) {
       return this.visit(ctx.expression());
    }
    

    @Override
    public AGenericCDFGNode visitIfStatement(IfStatementContext ctx) {
            
        AGenericCDFGNode condition_node = this.visit(ctx.condition);
        
        for(var if_statement : ctx.ifsats) {
            this.icdfg.addEdge(condition_node, this.visit(if_statement), true);
        }
        
        return condition_node;
    }

 
    @Override
    public AGenericCDFGNode visitIfElseStatement(IfElseStatementContext ctx) {
        
        AGenericCDFGNode condition_node = this.visit(ctx.condition);
          
        for(var if_statement : ctx.ifsats) {
            this.icdfg.addEdge(condition_node, this.visit(if_statement), true);
        }
        
        for(var else_statement : ctx.elsestats) {
            this.icdfg.addEdge(condition_node, this.visit(else_statement), false);
        }
        
        return condition_node;
    }
   
    // Expression level ///////////////////////////////////////////////////////
    @Override
    public AGenericCDFGNode visitVariableExpr(VariableExprContext ctx) {                
        return this.visit(ctx.operand());
    }
    
  
    @Override
    public AGenericCDFGNode visitAssignmentExpr(AssignmentExprContext ctx) {
         
        AGenericCDFGNode output_node = this.visit(ctx.left);
        AGenericCDFGNode expression_node = this.visit(ctx.right);
       
        output_node = this.icdfg.addOutputNode(output_node);  
        expression_node = this.icdfg.addInputNode(expression_node);       
               
        this.icdfg.addNode(output_node);
        this.icdfg.addNode(expression_node);               
        
        this.icdfg.addEdge(expression_node, output_node);
          
        return expression_node;
    }
    
 
    @Override
    public AGenericCDFGNode visitBinaryExpr(BinaryExprContext ctx) {

    AGenericCDFGNode operator_node = this.icdfg.addNode(this.visit(ctx.operator()));
    AGenericCDFGNode expression_node_left = this.visit(ctx.left);
    AGenericCDFGNode expression_node_right = this.visit(ctx.right);
    
    expression_node_left = this.icdfg.addInputNode(expression_node_left);
    expression_node_right = this.icdfg.addInputNode(expression_node_right);  
    
    this.icdfg.addNode(expression_node_left);  
    this.icdfg.addNode(expression_node_right);
    
    this.icdfg.addEdge(expression_node_left, operator_node);
    this.icdfg.addEdge(expression_node_right, operator_node);
    
    return operator_node; 
    }

    @Override
    public AGenericCDFGNode visitUnaryExpr(UnaryExprContext ctx) {

        AGenericCDFGNode operator_node = this.icdfg.addNode(this.visit(ctx.operator()));    
        AGenericCDFGNode expression_node = this.visit(ctx.right);
        
        expression_node = this.icdfg.addInputNode(expression_node);
        
        this.icdfg.addNode(expression_node);
         
        this.icdfg.addEdge(expression_node, operator_node);
        
        return operator_node;     
    }

    @Override
    public AGenericCDFGNode visitParenExpr(ParenExprContext ctx) {
        return this.visit(ctx.expression());
    }

    @Override
    public AGenericCDFGNode visitFunctionExpr(FunctionExprContext ctx) {
       
        AGenericCDFGNode function_node;
        AGenericCDFGNode argument_node = this.icdfg.addNode(this.visit(ctx.arguments()));
        String function_name = ctx.functionName().getText();
        
        if(InstructionCDFGGeneratorMaps.FUNCTION_MAP.containsKey(function_name)) {
            function_node = InstructionCDFGGeneratorMaps.FUNCTION_MAP.get(function_name).get().apply();
            this.icdfg.addNode(function_node);
            this.icdfg.addEdge(argument_node, function_node);
            return function_node;
        } 
        
        return argument_node;
    }
    
    @Override
    public AGenericCDFGNode visitArguments(ArgumentsContext ctx) {     
        return this.visit(ctx.expression(0));
    }

    // Operator level /////////////////////////////////////////////////////////
    
    @Override
    public AGenericCDFGNode visitOperator(OperatorContext ctx) {    
        return InstructionCDFGGeneratorMaps.OPERATION_MAP.get(ctx.getText()).get().apply();
    }

    // Operand level //////////////////////////////////////////////////////////
    
    @Override
    public AGenericCDFGNode visitAsmField(AsmFieldContext ctx) {      
         return new GenericCDFGVariableNode(ctx.getText());
    }
    @Override
    public AGenericCDFGNode visitLiteral(LiteralContext ctx) {
        return new GenericCDFGLiteralNode(ctx.getText());
    }

    @Override
    public AGenericCDFGNode visitMetaField(MetaFieldContext ctx) {
        return new GenericCDFGVariableNode(ctx.meta_field().processorRegister.getText());
    }
/*
    @Override
    public RangeSubscriptOperandASTNode visitRangesubscriptOperand(RangesubscriptOperandContext ctx) {
        var loidx = Integer.valueOf(ctx.loidx.getText());
        var hiidx = Integer.valueOf(ctx.hiidx.getText());
        return new RangeSubscriptOperandASTNode(
                (OperandASTNode) this.visit(ctx.operand()), loidx, hiidx);
    }

    @Override
    public ScalarSubscriptOperandASTNode visitScalarsubscriptOperand(ScalarsubscriptOperandContext ctx) {
        var idx = Integer.valueOf(ctx.idx.getText());
        return new ScalarSubscriptOperandASTNode(
                (OperandASTNode) this.visit(ctx.operand()), idx);
    }
  */  
}
