package pt.up.fe.specs.binarytranslation.instruction.ast.passes;

import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.BinaryExpressionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.ExpressionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.OperandASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.OperatorASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.PseudoInstructionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.StatementASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.UnaryExpressionASTNode;

public abstract class InstructionASTVisitor<T> {

    protected abstract T visitChildren(InstructionASTNode node);

    protected T visit(PseudoInstructionASTNode node) {
        return this.visitChildren(node);
    };

    protected T visit(StatementASTNode node) {
        return this.visitChildren(node);
    };

    protected T visit(BinaryExpressionASTNode node) {
        return this.visitChildren(node);
    };

    protected T visit(UnaryExpressionASTNode node) {
        return this.visitChildren(node);
    };

    protected T visit(OperandASTNode node) {
        return this.visitChildren(node);
    };

    protected T visit(OperatorASTNode node) {
        return this.visitChildren(node);
    };

    protected T visit(ExpressionASTNode node) {

        if (node instanceof BinaryExpressionASTNode) {
            return this.visit((BinaryExpressionASTNode) node);
        }

        else if (node instanceof UnaryExpressionASTNode) {
            return this.visit((UnaryExpressionASTNode) node);
        }

        else
            return null;
    }

    protected T visit(InstructionASTNode node) {

        if (node instanceof ExpressionASTNode) {
            return this.visit((ExpressionASTNode) node);
        }

        else if (node instanceof PseudoInstructionASTNode) {
            return this.visit((PseudoInstructionASTNode) node);
        }

        else if (node instanceof StatementASTNode) {
            return this.visit((StatementASTNode) node);
        }

        else if (node instanceof OperandASTNode) {
            return this.visit((OperandASTNode) node);
        }

        else if (node instanceof OperatorASTNode) {
            return this.visit((OperatorASTNode) node);
        }

        else
            return null;
    }
}
