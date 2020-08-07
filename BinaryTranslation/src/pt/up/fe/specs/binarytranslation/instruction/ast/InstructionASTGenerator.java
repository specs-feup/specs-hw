package pt.up.fe.specs.binarytranslation.instruction.ast;

import java.util.ArrayList;

import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.OperatorASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.PseudoInstructionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.expr.AssignmentExpressionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.expr.BinaryExpressionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.expr.ExpressionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.expr.RangeSubscriptExpressionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.expr.ScalarSubscriptExpressionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.expr.UnaryExpressionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.operand.BareOperandASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.operand.LiteralOperandASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.operand.OperandASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.statement.IfElseStatementASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.statement.IfStatementASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.statement.PlainStatementASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.statement.StatementASTNode;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionBaseVisitor;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.AsmFieldContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.AssignmentExprContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.BinaryExprContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.IfElseStatementContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.IfStatementContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.LiteralContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.OperatorContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.ParenExprContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.PlainStmtContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.PseudoInstructionContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.RangesubscriptExprContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.ScalarsubscriptExprContext;
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
        return new AssignmentExpressionASTNode((OperandASTNode) this.visit(ctx.operand()),
                (ExpressionASTNode) this.visit(ctx.expression()));
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
    public InstructionASTNode visitScalarsubscriptExpr(ScalarsubscriptExprContext ctx) {
        var idx = Integer.valueOf(ctx.idx.getText());
        return new ScalarSubscriptExpressionASTNode((OperandASTNode) this.visit(ctx.operand()), idx);
    }

    @Override
    public InstructionASTNode visitRangesubscriptExpr(RangesubscriptExprContext ctx) {
        var loidx = Integer.valueOf(ctx.loidx.getText());
        var hiidx = Integer.valueOf(ctx.hiidx.getText());
        return new RangeSubscriptExpressionASTNode((OperandASTNode) this.visit(ctx.operand()), loidx, hiidx);
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
}
