package pt.up.fe.specs.binarytranslation.instruction.ast.passes;

import pt.up.fe.specs.binarytranslation.instruction.ast.InstructionAST;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.OperatorASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.PseudoInstructionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.expr.AssignmentExpressionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.expr.BinaryExpressionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.expr.ExpressionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.expr.UnaryExpressionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.operand.BareOperandASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.operand.LiteralOperandASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.operand.OperandASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.transformed.ConcreteOperandASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.transformed.ImmediateOperandASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.transformed.VariableOperandASTNode;

public abstract class InstructionASTVisitor<T> {

    // TODO add missing node types

    public T visit(InstructionAST ast) {
        return this.visit(ast.getRootnode());
    };

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

    /////////////////////////////////////////////////////////////////////////

    protected T visit(InstructionASTNode node) {

        if (node instanceof ExpressionASTNode) {
            return this.visit((ExpressionASTNode) node);
        }

        else if (node instanceof PseudoInstructionASTNode) {
            return this.visit((PseudoInstructionASTNode) node);
        }

        else if (node instanceof AssignmentExpressionASTNode) {
            return this.visit((AssignmentExpressionASTNode) node);
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

    /////////////////////////////////////////////////////////////////////////

    protected T visit(PseudoInstructionASTNode node) {
        return this.visitChildren(node);
    };

    protected T visit(AssignmentExpressionASTNode node) {
        return this.visitChildren(node);
    };

    /////////////////////////////////////////////////////////////////////////

    protected T visit(ExpressionASTNode node) {

        if (node instanceof BinaryExpressionASTNode)
            return this.visit((BinaryExpressionASTNode) node);

        else if (node instanceof UnaryExpressionASTNode)
            return this.visit((UnaryExpressionASTNode) node);

        else if (node instanceof OperandASTNode)
            return this.visit((OperandASTNode) node);

        else
            return null;
    }

    protected T visit(BinaryExpressionASTNode node) {
        return this.visitChildren(node);
    };

    protected T visit(UnaryExpressionASTNode node) {
        return this.visitChildren(node);
    };

    /////////////////////////////////////////////////////////////////////////

    protected T visit(OperandASTNode node) {

        if (node instanceof ConcreteOperandASTNode)
            return this.visit((ConcreteOperandASTNode) node);

        else if (node instanceof BareOperandASTNode)
            return this.visit((BareOperandASTNode) node);

        else if (node instanceof LiteralOperandASTNode)
            return this.visit((LiteralOperandASTNode) node);

        else
            return this.visitChildren(node);
    };

    protected T visit(ConcreteOperandASTNode node) {

        if (node instanceof VariableOperandASTNode) {
            return this.visit((VariableOperandASTNode) node);
        }

        else if (node instanceof ImmediateOperandASTNode) {
            return this.visit((ImmediateOperandASTNode) node);
        }

        else
            return null;
    }

    protected T visit(BareOperandASTNode node) {
        return this.visitChildren(node);
    }

    protected T visit(LiteralOperandASTNode node) {
        return this.visitChildren(node);
    }

    protected T visit(VariableOperandASTNode node) {
        return this.visitChildren(node);
    }

    protected T visit(ImmediateOperandASTNode node) {
        return this.visitChildren(node);
    }

    /////////////////////////////////////////////////////////////////////////

    protected T visit(OperatorASTNode node) {
        return this.visitChildren(node);
    };
}
