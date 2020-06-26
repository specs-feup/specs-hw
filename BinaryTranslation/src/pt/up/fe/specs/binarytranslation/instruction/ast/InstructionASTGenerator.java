package pt.up.fe.specs.binarytranslation.instruction.ast;

import java.util.ArrayList;

import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.BinaryExpressionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.OperandASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.OperatorASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.PseudoInstructionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.StatementASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.UnaryExpressionASTNode;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionBaseVisitor;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.AsmFieldContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.BinaryOperationContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.LiteralContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.OperatorContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.ParenExpressionContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.PseudoInstructionContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.StatementContext;

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
    public InstructionASTNode visitPseudoInstruction(PseudoInstructionContext ctx) {
        var statements = new ArrayList<InstructionASTNode>();
        for (var s : ctx.statement()) {
            statements.add(this.visit(s));
        }
        return new PseudoInstructionASTNode(statements);
    }

    @Override
    public InstructionASTNode visitStatement(StatementContext ctx) {
        return new StatementASTNode(this.visit(ctx.operand()), this.visit(ctx.expression()));
    }

    @Override
    public InstructionASTNode visitAsmField(AsmFieldContext ctx) {
        return new OperandASTNode(ctx.getText());
    }

    @Override
    public InstructionASTNode visitLiteral(LiteralContext ctx) {
        return new OperandASTNode(ctx.getText());
    }

    @Override
    public InstructionASTNode visitOperator(OperatorContext ctx) {
        return new OperatorASTNode(ctx.getText());
    }

    @Override
    public InstructionASTNode visitBinaryOperation(BinaryOperationContext ctx) {
        return new BinaryExpressionASTNode(this.visit(ctx.left),
                this.visit(ctx.operator()), this.visit(ctx.right));
    }

    @Override
    public InstructionASTNode visitParenExpression(ParenExpressionContext ctx) {
        return new UnaryExpressionASTNode(this.visit(ctx.expression()));
    }
}
