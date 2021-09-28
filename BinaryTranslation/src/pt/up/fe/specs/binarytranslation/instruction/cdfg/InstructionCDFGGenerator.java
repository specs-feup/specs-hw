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

package pt.up.fe.specs.binarytranslation.instruction.cdfg;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.base.edges.InstructionCDFGDataEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.base.nodes.AInstructionCDFGNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.base.nodes.InstructionCDFGNodeGenerator;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.base.nodes.data.InstructionCDFGLiteral;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.base.nodes.data.InstructionCDFGRegister;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.base.nodes.expression.arithmetic.InstructionCDFGAddition;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.base.nodes.expression.arithmetic.InstructionCDFGDivision;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.base.nodes.expression.arithmetic.InstructionCDFGMultiplication;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.base.nodes.expression.arithmetic.InstructionCDFGShiftLeftLogical;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.base.nodes.expression.arithmetic.InstructionCDFGShiftRightArithmetic;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.base.nodes.expression.arithmetic.InstructionCDFGShiftRightLogical;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.base.nodes.expression.arithmetic.InstructionCDFGSquareRoot;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.base.nodes.expression.arithmetic.InstructionCDFGSubtraction;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.base.nodes.expression.bitwise.InstructionCDFGAnd;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.base.nodes.expression.bitwise.InstructionCDFGNot;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.base.nodes.expression.bitwise.InstructionCDFGOr;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.base.nodes.expression.bitwise.InstructionCDFGXor;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.base.nodes.expression.comparison.InstructionCDFGEqualsTo;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.base.nodes.expression.comparison.InstructionCDFGGreaterThan;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.base.nodes.expression.comparison.InstructionCDFGGreaterThanOrEqualsTo;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.base.nodes.expression.comparison.InstructionCDFGLessThan;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.base.nodes.expression.comparison.InstructionCDFGLessThanOrEqualsTo;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.base.nodes.expression.comparison.InstructionCDFGNotEqualsTo;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.base.nodes.expression.logical.InstructionCDFGLogicalAnd;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.base.nodes.expression.logical.InstructionCDFGLogicalOr;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionBaseVisitor;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.ArgumentsContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.AsmFieldContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.AssignmentExprContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.BinaryExprContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.FunctionExprContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.IfElseStatementContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.LiteralContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.MetaFieldContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.OperatorContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.ParenExprContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.PlainStmtContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.PseudoInstructionContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.UnaryExprContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.VariableExprContext;

/*
 * Really bad implementation of generation of a ICDFG
 *      It creates a new graph for each expression and token to be added to the final graph
 *      Then in the parent token it joins the created graph with the parent's graph (creates the appropriate edges)
 * 
 * The methods for detecting nodes without targets or sources is terrible
 *      Maybe the InstructionCDFG should be aware of nodes in this position 
 *          And then maybe the edges can be create without explicitly joining the graphs ? (i don't know if that is allowed of not)
 *      
 *  The OPERATIONMAP doesn't work as it doesn't allow the classes to be used as types for the graph
 *  
 *  
 *  NOTES:
 *      I think maybe the setCarry and getcarry functions should be eliminated and an inferred register (ex: $carry) should be used
 *          This is because it would make generating the use of the carry simpler, since it would be used as any other register
 *              RD=RA+RB+getCarry() ==> RD=RA+RB+$carry;
 *              setCarry(msb(RD)) ==> $carry = msb(RD) 
 *              
 *      Also if the Operators are parents of the corresponding operands of a BinaryExpression, it would make generating the DFG/CDFG much easiers,
 *          since it the hiearchy would be the same as in those graphs
 */

public class InstructionCDFGGenerator extends PseudoInstructionBaseVisitor<AInstructionCDFGNode>{

    private InstructionCDFG icdfg;
    
    private static final Map<String, Supplier<InstructionCDFGNodeGenerator>> OPERATIONMAP;
    
    static {
        var amap = new HashMap<String,  Supplier<InstructionCDFGNodeGenerator>>();
        
        //Expressions
         
        //Arithmetic Nodes
        amap.put("+", InstructionCDFGAddition::new);
        amap.put("-", InstructionCDFGSubtraction::new);
        amap.put("*", InstructionCDFGMultiplication::new);
        amap.put("/", InstructionCDFGDivision::new);
        amap.put("sqrt", InstructionCDFGSquareRoot::new);
        amap.put("<<", InstructionCDFGShiftLeftLogical::new);
        amap.put(">>", InstructionCDFGShiftRightLogical::new);
        amap.put(">>>", InstructionCDFGShiftRightArithmetic::new);
        
        //Bitwise Nodes
       amap.put("&", InstructionCDFGAnd::new);
       amap.put("^", InstructionCDFGXor::new);
       amap.put("|", InstructionCDFGOr::new);
       
       //Comparison Nodes
       amap.put("==", InstructionCDFGEqualsTo::new);
       amap.put("!=", InstructionCDFGNotEqualsTo::new);
       amap.put(">", InstructionCDFGGreaterThan::new);
       amap.put(">=", InstructionCDFGGreaterThanOrEqualsTo::new);
       amap.put("<", InstructionCDFGLessThan::new);
       amap.put("<=", InstructionCDFGLessThanOrEqualsTo::new);
       
       //Logical Nodes
       amap.put("&&", InstructionCDFGLogicalAnd::new);
       amap.put("||", InstructionCDFGLogicalOr::new);
       amap.put("~", InstructionCDFGNot::new);
       
       //Functions
       
       
       
       //Data
        
      
       
        OPERATIONMAP = Collections.unmodifiableMap(amap);
    }
   
    /** Method for generating an InstructionCDFG from the ParseTree of an Instruction
     * 
     * @param inst
     * @return The InstructionCDFG corresponding to the passed Instruction
     */
public InstructionCDFG generate(Instruction inst) {
        
       
        this.icdfg = new InstructionCDFG();
       // return this.visit(inst.getPseudocode().getParseTree());
        this.visitPseudoInstructionAST(inst.getPseudocode().getParseTree());
     
        return this.icdfg;
        /*
        var graph = new InstructionDFG(); //empty
        var tree = inst.getPseudocode().getParseTree();
        
        for (var s : tree.statement()) {
            var nodes = visitStatement(s);
            for(var n : nodes)
                graph.addVertex(n, ??
        }*/
        
        //var graph = new InstructionDFG(); //empty
     //   var tree = inst.getPseudocode().getParseTree();
     //   var s1 = tree.statement().get(0);
                
        
     //   return graph = visitStatement(s1);
        
    }

    
 // Top level //////////////////////////////////////////////////////////////
    public InstructionCDFG visitPseudoInstructionAST(PseudoInstructionContext ctx) {
        
        //InstructionCDFG pseudoinstruction_icdfg = new InstructionCDFG();
        
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
     public AInstructionCDFGNode visitPlainStmt(PlainStmtContext ctx) {
       return this.visit(ctx.expression());
    }
    

   /* @Override
    public InstructionCDFG visitIfStatement(IfStatementContext ctx) {
            
            
        
        return null;
    }

*/ 
    @Override
    public AInstructionCDFGNode visitIfElseStatement(IfElseStatementContext ctx) {
        
        AInstructionCDFGNode condition_node = this.visit(ctx.condition);
          
        for(var if_statement : ctx.ifsats) {
            System.out.println(if_statement.getText());
            this.icdfg.addEdge(condition_node, this.visit(if_statement), true);
 
        }
        
        for(var else_statement : ctx.elsestats) {
            this.icdfg.addEdge(condition_node, this.visit(else_statement), false);
        }
        
        return condition_node;
    }
   
    // Expression level ///////////////////////////////////////////////////////
    @Override
    public AInstructionCDFGNode visitVariableExpr(VariableExprContext ctx) {                
        return this.visit(ctx.operand());
    }
    
  
    @Override
    public AInstructionCDFGNode visitAssignmentExpr(AssignmentExprContext ctx) {
         
        AInstructionCDFGNode output_node = this.icdfg.addNode(this.visit(ctx.left));
        AInstructionCDFGNode expression_node = this.icdfg.addNode(this.visit(ctx.right));
        
       
        
        if(ctx.right instanceof FunctionExprContext) {
            this.icdfg.addEdge(expression_node, output_node,((FunctionExprContext)ctx.right).functionName().getText() + "()");
        }else {
            this.icdfg.addEdge(expression_node, output_node);
        }

        
        return expression_node;
    }
    
 
    @Override
    public AInstructionCDFGNode visitBinaryExpr(BinaryExprContext ctx) {
        
      AInstructionCDFGNode operator_node = this.icdfg.addNode(this.visit(ctx.operator()));
      AInstructionCDFGNode expression_node_left = this.icdfg.addNode(this.visit(ctx.left));
      AInstructionCDFGNode expression_node_right = this.icdfg.addNode(this.visit(ctx.right));
      
      this.icdfg.addEdge(expression_node_left, operator_node);
      this.icdfg.addEdge(expression_node_right, operator_node);
      
      return operator_node;
    }

    @Override
    public AInstructionCDFGNode visitUnaryExpr(UnaryExprContext ctx) {

        AInstructionCDFGNode operator_node = this.icdfg.addNode(this.visit(ctx.operator()));    
        AInstructionCDFGNode expression_node = this.icdfg.addNode(this.visit(ctx.right));

        this.icdfg.addEdge(expression_node, operator_node);
        
        return operator_node;
    }

    @Override
    public AInstructionCDFGNode visitParenExpr(ParenExprContext ctx) {
        return this.visit(ctx.expression());
    }

    @Override
    public AInstructionCDFGNode visitFunctionExpr(FunctionExprContext ctx) {
        return this.visit(ctx.arguments());
    }
    
    @Override
    public AInstructionCDFGNode visitArguments(ArgumentsContext ctx) {     
        return this.visit(ctx.expression(0));
    }

    // Operator level /////////////////////////////////////////////////////////
    
    @Override
    public AInstructionCDFGNode visitOperator(OperatorContext ctx) {    
        return this.icdfg.addNode(OPERATIONMAP.get(ctx.getText()).get().apply());
    }

    // Operand level //////////////////////////////////////////////////////////
    
    @Override
    public AInstructionCDFGNode visitAsmField(AsmFieldContext ctx) {      
         return this.icdfg.addNode(new InstructionCDFGRegister(ctx.getText()));
    }
    @Override
    public AInstructionCDFGNode visitLiteral(LiteralContext ctx) {
        return this.icdfg.addNode(new InstructionCDFGLiteral(ctx.getText()));
    }

    @Override
    public AInstructionCDFGNode visitMetaField(MetaFieldContext ctx) {
        return this.icdfg.addNode(new InstructionCDFGRegister("$" + ctx.meta_field().processorRegister.getText()));
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
