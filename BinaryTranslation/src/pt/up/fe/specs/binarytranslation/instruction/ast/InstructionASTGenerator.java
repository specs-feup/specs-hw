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
 
package pt.up.fe.specs.binarytranslation.instruction.ast;

import java.util.ArrayList;

import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNode;
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
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionBaseVisitor;
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
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.RangesubscriptOperandContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.ScalarsubscriptOperandContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.UnaryExprContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.VariableExprContext;

/**
 * Builds an AST
 * 
 * @author nuno
 *
 */
public class InstructionASTGenerator extends PseudoInstructionBaseVisitor<InstructionASTNode> {

    public InstructionASTGenerator() {
        // TODO Auto-generated constructor stub
    }

    // Top level //////////////////////////////////////////////////////////////
    @Override
    public PseudoInstructionASTNode visitPseudoInstruction(PseudoInstructionContext ctx) {
        var statements = new ArrayList<StatementASTNode>();
        for (var s : ctx.statement()) {
            statements.add((StatementASTNode) this.visit(s));
        }
        return new PseudoInstructionASTNode(statements);
    }

    // Statement level ////////////////////////////////////////////////////////
    /*    @Override
    public InstructionASTNode visitStatement(StatementContext ctx) {
        return new StatementASTNode((ExpressionASTNode) visit(ctx.expression()));
    }*/

    @Override
    public InstructionASTNode visitPlainStmt(PlainStmtContext ctx) {
        return new PlainStatementASTNode((ExpressionASTNode) visit(ctx.expression()));
    }

    @Override
    public InstructionASTNode visitIfStatement(IfStatementContext ctx) {

        var stats = new ArrayList<StatementASTNode>();
        for (var stat : ctx.ifsats) {
            stats.add((StatementASTNode) this.visit(stat));
        }

        return new IfStatementASTNode((ExpressionASTNode) this.visit(ctx.condition), stats);
    }

    @Override
    public InstructionASTNode visitIfElseStatement(IfElseStatementContext ctx) {

        var stats = new ArrayList<StatementASTNode>();
        for (var stat : ctx.ifsats) {
            stats.add((StatementASTNode) this.visit(stat));
        }

        var estats = new ArrayList<StatementASTNode>();
        for (var estat : ctx.elsestats) {
            stats.add((StatementASTNode) this.visit(estat));
        }

        return new IfElseStatementASTNode((ExpressionASTNode) this.visit(ctx.condition), stats, estats);
    }

    // Expression level ///////////////////////////////////////////////////////
    @Override
    public InstructionASTNode visitVariableExpr(VariableExprContext ctx) {
        return this.visit(ctx.operand());
    }

    @Override
    public InstructionASTNode visitAssignmentExpr(AssignmentExprContext ctx) {
        return new AssignmentExpressionASTNode((OperandASTNode) this.visit(ctx.left),
                (ExpressionASTNode) this.visit(ctx.right));
    }

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

    @Override
    public InstructionASTNode visitFunctionExpr(FunctionExprContext ctx) {

        var args = ctx.arguments();
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
    public OperatorASTNode visitOperator(OperatorContext ctx) {
        return new OperatorASTNode(ctx.getText());
    }

    // Operand level //////////////////////////////////////////////////////////
    @Override
    public BareOperandASTNode visitAsmField(AsmFieldContext ctx) {
        return new BareOperandASTNode(ctx.getText());
    }

    @Override
    public LiteralOperandASTNode visitLiteral(LiteralContext ctx) {
        return new LiteralOperandASTNode(Integer.valueOf(ctx.getText()));
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
