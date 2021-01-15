package pt.up.fe.specs.binarytranslation.instruction.ast.passes;

import pt.up.fe.specs.binarytranslation.instruction.ast.InstructionAST;
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
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.transformed.ConcreteOperandASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.transformed.ImmediateOperandASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.transformed.VariableOperandASTNode;

public abstract class InstructionASTVisitor<T> {

    /*
     * Default always returns result of last child visited
     */
    protected T visitChildren(InstructionASTNode node) throws Exception {
        T ret = null;
        for (var c : node.getChildren()) {
            ret = this.visit(c);
        }
        return ret;
    }

    public T visit(InstructionAST ast) throws Exception {
        return this.visitChildren(ast.getRootnode());
    };

    // Abstracts //////////////////////////////////////////////////////////////

    protected T visit(InstructionASTNode node) throws Exception {

        if (node instanceof PseudoInstructionASTNode) {
            return this.visit((PseudoInstructionASTNode) node);
        }

        else if (node instanceof StatementASTNode) {
            return this.visit((StatementASTNode) node);
        }

        else if (node instanceof ExpressionASTNode) {
            return this.visit((ExpressionASTNode) node);
        }

        else if (node instanceof ExpressionASTNode) {
            return this.visit((ExpressionASTNode) node);
        }

        else if (node instanceof OperatorASTNode) {
            return this.visit((OperatorASTNode) node);
        }

        else
            return null;
    }

    /////////////////////////////////////////////////////////////////////////

    protected T visit(PseudoInstructionASTNode node) throws Exception {
        return this.visitChildren(node);
    };

    /////////////////////////////////////////////////////////////////////////

    protected T visit(StatementASTNode node) throws Exception {

        if (node instanceof PlainStatementASTNode)
            return this.visit((PlainStatementASTNode) node);

        else if (node instanceof IfStatementASTNode)
            return this.visit((IfStatementASTNode) node);

        else if (node instanceof IfElseStatementASTNode)
            return this.visit((IfElseStatementASTNode) node);

        else
            return null;
    }

    protected T visit(PlainStatementASTNode node) throws Exception {
        return this.visitChildren(node);
    };

    protected T visit(IfStatementASTNode node) throws Exception {
        return this.visitChildren(node);
    };

    protected T visit(IfElseStatementASTNode node) throws Exception {
        return this.visitChildren(node);
    };

    /////////////////////////////////////////////////////////////////////////

    protected T visit(ExpressionASTNode node) throws Exception {

        if (node instanceof AssignmentExpressionASTNode)
            return this.visit((AssignmentExpressionASTNode) node);

        else if (node instanceof BinaryExpressionASTNode)
            return this.visit((BinaryExpressionASTNode) node);

        else if (node instanceof UnaryExpressionASTNode)
            return this.visit((UnaryExpressionASTNode) node);

        else if (node instanceof FunctionExpressionASTNode)
            return this.visit(node);

        else if (node instanceof OperandASTNode)
            return this.visit((OperandASTNode) node);

        /* else if (node instanceof ExpressionASTNode)
            return this.visit(node);*/

        return null;
    }

    protected T visit(AssignmentExpressionASTNode node) throws Exception {
        return this.visitChildren(node);
    };

    protected T visit(BinaryExpressionASTNode node) throws Exception {
        return this.visitChildren(node);
    };

    protected T visit(UnaryExpressionASTNode node) throws Exception {
        return this.visitChildren(node);
    };

    protected T visit(FunctionExpressionASTNode node) throws Exception {
        return this.visitChildren(node);
    };

    /////////////////////////////////////////////////////////////////////////

    protected T visit(OperandASTNode node) throws Exception {

        if (node instanceof ConcreteOperandASTNode)
            return this.visit((ConcreteOperandASTNode) node);

        else if (node instanceof BareOperandASTNode)
            return this.visit((BareOperandASTNode) node);

        else if (node instanceof LiteralOperandASTNode)
            return this.visit((LiteralOperandASTNode) node);

        else if (node instanceof MetaOperandASTNode)
            return this.visit((MetaOperandASTNode) node);

        else if (node instanceof ScalarSubscriptOperandASTNode)
            return this.visit((ScalarSubscriptOperandASTNode) node);

        else if (node instanceof RangeSubscriptOperandASTNode)
            return this.visit((RangeSubscriptOperandASTNode) node);

        else
            return this.visitChildren(node);
    };

    protected T visit(ConcreteOperandASTNode node) throws Exception {

        if (node instanceof VariableOperandASTNode) {
            return this.visit((VariableOperandASTNode) node);
        }

        else if (node instanceof ImmediateOperandASTNode) {
            return this.visit((ImmediateOperandASTNode) node);
        }

        else
            return null;
    }

    protected T visit(BareOperandASTNode node) throws Exception {
        return this.visitChildren(node);
    }

    protected T visit(LiteralOperandASTNode node) throws Exception {
        return this.visitChildren(node);
    }

    protected T visit(MetaOperandASTNode node) throws Exception {
        return this.visitChildren(node);
    }

    protected T visit(VariableOperandASTNode node) throws Exception {
        return this.visitChildren(node);
    }

    protected T visit(ImmediateOperandASTNode node) throws Exception {
        return this.visitChildren(node);
    }

    protected T visit(ScalarSubscriptOperandASTNode node) throws Exception {
        return this.visitChildren(node);
    };

    protected T visit(RangeSubscriptOperandASTNode node) throws Exception {
        return this.visitChildren(node);
    };

    /////////////////////////////////////////////////////////////////////////

    protected T visit(OperatorASTNode node) throws Exception {
        return this.visitChildren(node);
    };
}
