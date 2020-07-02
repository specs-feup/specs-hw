package pt.up.fe.specs.binarytranslation.instruction.ast.passes;

import pt.up.fe.specs.binarytranslation.instruction.ast.InstructionAST;
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

public abstract class InstructionASTListener {

    public void visit(InstructionAST ast) {
        this.visit(ast.getRootnode());
    };

    protected void visit(PseudoInstructionASTNode node) {

        /*
         * Visit all StatementASTNodes
         */
        for (var c : node.getChildren())
            this.visit(c);
    };

    protected void visit(StatementASTNode node) {
        visit(node.getTarget());
        visit(node.getExpr());
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
        if (node instanceof InstructionOperandASTNode) {
            this.visit(node);
        }
    };

    protected void visit(InstructionOperandASTNode node) {

        if (node instanceof VariableOperandASTNode) {
            this.visit(node);
        }

        else if (node instanceof LiteralOperandASTNode) {
            this.visit(node);
        }
    }

    protected void visit(VariableOperandASTNode node) {

    }

    protected void visit(LiteralOperandASTNode node) {

    }

    protected void visit(OperatorASTNode node) {
    };

    protected void visit(ExpressionASTNode node) {

        if (node instanceof BinaryExpressionASTNode)
            this.visit((BinaryExpressionASTNode) node);

        else if (node instanceof UnaryExpressionASTNode)
            this.visit((UnaryExpressionASTNode) node);

        else if (node instanceof OperandASTNode)
            this.visit((OperandASTNode) node);

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
