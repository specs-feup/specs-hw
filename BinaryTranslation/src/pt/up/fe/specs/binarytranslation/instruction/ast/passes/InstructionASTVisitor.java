package pt.up.fe.specs.binarytranslation.instruction.ast.passes;

import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.BinaryExpressionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.ExpressionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.OperandASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.OperatorASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.PseudoInstructionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.StatementASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.UnaryExpressionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.transformed.InstructionOperandASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.transformed.LiteralOperandASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.transformed.VariableOperandASTNode;

public abstract class InstructionASTVisitor<T> {

    /*
     * Default always returns result of last child visited
     */
    protected T visitChildren(InstructionASTNode node) {
        T ret = null;
        for (var c : node.getChildren()) {
            ret = this.visit(c);
        }
        return ret;
    }

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

    protected T visit(InstructionOperandASTNode node) {

        if (node instanceof VariableOperandASTNode) {
            return this.visit(node);
        }

        else if (node instanceof LiteralOperandASTNode) {
            return this.visit(node);
        }

        else
            return null;
    }

    protected T visit(VariableOperandASTNode node) {
        return this.visitChildren(node);
    }

    protected T visit(LiteralOperandASTNode node) {
        return this.visitChildren(node);
    }

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
