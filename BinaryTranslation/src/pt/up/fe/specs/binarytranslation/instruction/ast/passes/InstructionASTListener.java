package pt.up.fe.specs.binarytranslation.instruction.ast.passes;

import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.BinaryExpressionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.ExpressionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.OperandASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.OperatorASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.PseudoInstructionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.StatementASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.UnaryExpressionASTNode;

public abstract class InstructionASTListener {

    protected void visit(PseudoInstructionASTNode node) {
    };

    protected void visit(StatementASTNode node) {
    };

    protected void visit(BinaryExpressionASTNode node) {
        visit(node.getLeft());
        visit(node.getOperator());
        visit(node.getRight());
    };

    protected void visit(UnaryExpressionASTNode node) {
        visit(node.getOperator());
        visit(node.getRight());
    };

    protected void visit(OperandASTNode node) {
    };

    protected void visit(OperatorASTNode node) {
    };

    protected void visit(ExpressionASTNode node) {

        if (node instanceof BinaryExpressionASTNode) {
            this.visit((BinaryExpressionASTNode) node);
        }

        else if (node instanceof UnaryExpressionASTNode) {
            this.visit((UnaryExpressionASTNode) node);
        }

        return;
    }

    protected void visit(InstructionASTNode node) {

        if (node instanceof ExpressionASTNode) {
            this.visit((ExpressionASTNode) node);
        }

        else if (node instanceof PseudoInstructionASTNode) {
            this.visit((PseudoInstructionASTNode) node);
        }

        else if (node instanceof StatementASTNode) {
            this.visit((StatementASTNode) node);
        }

        else if (node instanceof OperandASTNode) {
            this.visit((OperandASTNode) node);
        }

        else if (node instanceof OperatorASTNode) {
            this.visit((OperatorASTNode) node);
        }

        return;
    }
}
