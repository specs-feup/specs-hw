package pt.up.fe.specs.binarytranslation.instruction.ast;

import java.util.ArrayList;

import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.BinaryExpressionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.ExpressionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.OperandASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.OperatorASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.PseudoInstructionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.StatementASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.UnaryExpressionASTNode;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionBaseVisitor;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.AsmFieldContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.BinaryOperationContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.LiteralContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.OperatorContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.ParenExpressionContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.PseudoInstructionContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.StatementContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.UnaryOperationContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.VariableContext;

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

    @Override
    public PseudoInstructionASTNode visitPseudoInstruction(PseudoInstructionContext ctx) {
        var statements = new ArrayList<StatementASTNode>();
        for (var s : ctx.statement()) {
            statements.add((StatementASTNode) this.visit(s));
        }
        return new PseudoInstructionASTNode(statements);
    }

    @Override
    public StatementASTNode visitStatement(StatementContext ctx) {
        return new StatementASTNode((OperandASTNode) this.visit(ctx.operand()),
                (ExpressionASTNode) this.visit(ctx.expression()));
    }

    @Override
    public InstructionASTNode visitVariable(VariableContext ctx) {
        return this.visit(ctx.operand());
    }

    @Override
    public OperandASTNode visitAsmField(AsmFieldContext ctx) {
        return new OperandASTNode(ctx.getText());
    }

    @Override
    public OperandASTNode visitLiteral(LiteralContext ctx) {
        return new OperandASTNode(ctx.getText());
    }

    @Override
    public OperatorASTNode visitOperator(OperatorContext ctx) {
        return new OperatorASTNode(ctx.getText());
    }

    @Override
    public InstructionASTNode visitBinaryOperation(BinaryOperationContext ctx) {
        return new BinaryExpressionASTNode((ExpressionASTNode) this.visit(ctx.left),
                (OperatorASTNode) this.visit(ctx.operator()), (ExpressionASTNode) this.visit(ctx.right));
    }

    @Override
    public InstructionASTNode visitUnaryOperation(UnaryOperationContext ctx) {
        return new UnaryExpressionASTNode((OperatorASTNode) this.visit(ctx.operator()),
                (ExpressionASTNode) this.visit(ctx.right));
    }

    @Override
    public InstructionASTNode visitParenExpression(ParenExpressionContext ctx) {
        var expr = ctx.expression();
        if (expr instanceof BinaryOperationContext)
            return this.visit(expr);

        else // if(expr instanceof UnaryOperationContext)
            return this.visit(expr);
    }
}
