package pt.up.fe.specs.binarytranslation.instruction.ast;

import java.util.ArrayList;

import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.OperandASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.PseudoInstructionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.StatementASTNode;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionBaseVisitor;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.AsmFieldContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.LiteralContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.OperatorContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.PseudoInstructionContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.StatementContext;

/**
 * Builds an AST using only generic string nodes (for testing of Parse Tree conversion)
 * 
 * @author nuno
 *
 */
public class GenericASTGenerator extends PseudoInstructionBaseVisitor<InstructionASTNode> {

    public GenericASTGenerator() {
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
        return new OperandASTNode(ctx.getText());
    }

    /*
    @Override
    public InstructionASTNode visitBinaryOperation(BinaryOperationContext ctx) {
        return new
    }*/
}
