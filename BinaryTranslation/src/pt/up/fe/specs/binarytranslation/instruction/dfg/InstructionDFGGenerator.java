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

package pt.up.fe.specs.binarytranslation.instruction.dfg;

import java.util.*;

import org.specs.MicroBlaze.parsing.*;
import org.specs.MicroBlaze.parsing.getters.MicroBlazeAsmOperandGetter.MicroBlazeAsmOperandParse;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.expr.*;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.operand.*;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.statement.*;
import pt.up.fe.specs.binarytranslation.instruction.dfg.nodes.InstructionDFGNode;
import pt.up.fe.specs.binarytranslation.instruction.dfg.nodes.base.data.InstructionDFGNodeData;
import pt.up.fe.specs.binarytranslation.instruction.dfg.nodes.base.operation.InstructionDFGOperationNode;
import pt.up.fe.specs.binarytranslation.instruction.dfg.nodes.base.operation.comparison.InstructionDFGComparisonOperationNode;
import pt.up.fe.specs.binarytranslation.instruction.dfg.nodes.base.operation.comparison.InstructionDFGComparisonOperationNodeType;
import pt.up.fe.specs.binarytranslation.instruction.dfg.nodes.base.operation.math.InstructionDFGMathOperationNode;
import pt.up.fe.specs.binarytranslation.instruction.dfg.nodes.base.operation.math.InstructionDFGMathOperationNodeType;
import pt.up.fe.specs.binarytranslation.instruction.operand.Operand;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.*;

public class InstructionDFGGenerator { //extends PseudoInstructionBaseListener {
    
    //private InstructionDFG newgraph;
    
    public InstructionDFGGenerator() {
        // TODO Auto-generated constructor stub
    }
    
    public InstructionDFG generateDFG(Instruction inst) {
        
        /*
        var graph = new InstructionDFG(); //empty
        var tree = inst.getPseudocode().getParseTree();
        
        for (var s : tree.statement()) {
            var nodes = visitStatement(s);
            for(var n : nodes)
                graph.addVertex(n, ??
        }*/
        
        //var graph = new InstructionDFG(); //empty
        var tree = inst.getPseudocode().getParseTree();
        var s1 = tree.statement().get(0);
                
        
        return graph = visitStatement(s1);
    }
    

    
 // Top level //////////////////////////////////////////////////////////////
    /*private List<InstructionDFGNode> /(PseudoInstructionContext ctx) {
        var statements = new ArrayList<StatementASTNode>();
        for (var s : ctx.statement()) {
            statements.add((StatementASTNode) this.visit(s));
        }
        return new PseudoInstructionASTNode(statements);
    }*/

    // Statement level ////////////////////////////////////////////////////////
    public InstructionDFG visitStatement(StatementContext ctx) {
        
        if(ctx instanceof PlainStmtContext)
            return visitPlainStmt((PlainStmtContext) ctx);
        else
            return null;
    }
    
    interface InstructionDFGNodeCreators {
        InstructionDFGNode apply(String operator);
    }
    
    private static final Map<String, InstructionDFGNodeCreators> OPERANDMAP;
    static {
        var amap = new HashMap<String, InstructionDFGNodeCreators>();
        amap.put("+", InstructionDFGAdder);
        amap.put("-", InstructionDFGSubtractor);
        ///
        OPERANDMAP = Collections.unmodifiableMap(amap);
    }
    
    public InstructionDFG visitPlainStmt(PlainStmtContext ctx) {

        var expr = (AssignmentExprContext) ctx.expression();
        var target =  expr.left;

        var arithmetic = (BinaryExprContext) expr.right;
        
        var lo = arithmetic.left.getText();
        var op = arithmetic.operator().getText();
        var ro = arithmetic.right.getText();
        
        var vlo = new InstructionDFGNodeData(lo);        
        var vop = OPERANDMAP.get(op).apply(op);        
        var vro = new InstructionDFGNodeData(ro);
        
        

        var graph = new InstructionDFG(); //empty
        graph.addVertex(vlo);
        graph.addEdge(vlo, vop);
        
    }
    
/*
    @Override
    public List<InstructionDFGNode> visitStatement(IfStatementContext ctx) {

        
    }

    @Override
    public InstructionDFGNode visitIfElseStatement(IfElseStatementContext ctx) {

        
    }*/

    
    // Expression level ///////////////////////////////////////////////////////
    @Override
    public InstructionDFGNode visitVariableExpr(VariableExprContext ctx) {
        
        /*
        var variable_type = InstructionDFGNodeDataType.getType(ctx.getText());
        
        if(variable_type.equals(InstructionDFGNodeDataType.Register)) {
            return new InstructionDFGRegisterNode(ctx.getText());
        }else if(variable_type.equals(InstructionDFGNodeDataType.Immediate)) {
            return new InstructionDFGImmediateNode(0);
        }else {
            return this.visit(ctx.operand());
        }*/              
        
        return this.visit(ctx.operand());
    }
    
    @Override
    public InstructionASTNode visitAssignmentExpr(AssignmentExprContext ctx) {
        return new AssignmentExpressionASTNode((OperandASTNode) this.visit(ctx.left),
                (ExpressionASTNode) this.visit(ctx.right));
        ctx.
    }
    /*
    @Override
    public InstructionASTNode visitBinaryExpr(BinaryExprContext ctx) {
        return new BinaryExpressionASTNode((ExpressionASTNode) this.visit(ctx.left),
                (OperatorASTNode) this.visit(ctx.operator()), (ExpressionASTNode) this.visit(ctx.right));
    }

    @Override
    public InstructionASTNode visitUnaryExpr(UnaryExprContext ctx) {
        return new UnaryExpressionASTNode((OperatorASTNode) this.visit(ctx.operator()),
                (ExpressionASTNode) this.visit(ctx.right));
    }

    @Override
    public InstructionASTNode visitParenExpr(ParenExprContext ctx) {
        return this.visit(ctx.expression());
    }
*/
    @Override
    public InstructionDFGNode visitFunctionExpr(FunctionExprContext ctx) {

        var args = ctx.arguments();
        ctx.get
        if (args == null)
            return new FunctionExpressionASTNode(ctx.functionName().getText());

        else {
            var arglist = new ArrayList<ExpressionASTNode>();
            for (var expr : args.expression()) {
                arglist.add((ExpressionASTNode) this.visit(expr));
            }

            return new FunctionExpressionASTNode(ctx.functionName().getText(), arglist);
        }
    }

    // Operator level /////////////////////////////////////////////////////////
    @Override
    public InstructionDFGOperationNode visitOperator(OperatorContext ctx) {
        
        String ctx_str = ctx.getText(); 
        
        if(InstructionDFGMathOperationNodeType.isMathOperator(ctx_str)) {
            return new InstructionDFGMathOperationNode(ctx_str);
        }else if(InstructionDFGComparisonOperationNodeType.isComparisonOperator(ctx_str)) {
            return new InstructionDFGComparisonOperationNode(ctx_str);
        }else {
            return null;
        }

    }

    // Operand level //////////////////////////////////////////////////////////
    @Override
    public BareOperandASTNode visitAsmField(AsmFieldContext ctx) {
        return new BareOperandDFGNode(ctx.getText());
    }

    @Override
    public LiteralOperandASTNode visitLiteral(LiteralContext ctx) {
        return new LiteralOperandDFGNode(Integer.valueOf(ctx.getText()));
    }

    @Override
    public MetaOperandASTNode visitMetaField(MetaFieldContext ctx) {
        return new MetaOperandASTNode(ctx.meta_field().processorRegister.getText());
    }

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
    
}
