/**
 * Copyright 2021 SPeCS.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */
 
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

public abstract class InstructionASTListener {

    protected void visitChildren(InstructionASTNode node) {
        for (var c : node.getChildren()) {
            this.visit(c);
        }
    }

    public void visit(InstructionAST ast) {
        this.visitChildren(ast.getRootnode());
    };

    // Abstracts //////////////////////////////////////////////////////////////

    protected void visit(InstructionASTNode node) {

        if (node instanceof PseudoInstructionASTNode) {
            this.visit((PseudoInstructionASTNode) node);
        }

        else if (node instanceof StatementASTNode) {
            this.visit((StatementASTNode) node);
        }

        else if (node instanceof ExpressionASTNode) {
            this.visit((ExpressionASTNode) node);
        }

        else if (node instanceof OperatorASTNode) {
            this.visit((OperatorASTNode) node);
        }

        return;
    }

    ///////////////////////////////////////////////////////////////////////////

    protected void visit(PseudoInstructionASTNode node) {
        this.visitChildren(node);
    };

    ///////////////////////////////////////////////////////////////////////////

    protected void visit(StatementASTNode node) {

        if (node instanceof PlainStatementASTNode)
            this.visit((PlainStatementASTNode) node);

        else if (node instanceof IfStatementASTNode)
            this.visit((IfStatementASTNode) node);

        else if (node instanceof IfElseStatementASTNode)
            this.visit((IfElseStatementASTNode) node);

        return;
    };

    protected void visit(PlainStatementASTNode node) {
        this.visitChildren(node);
    };

    protected void visit(IfStatementASTNode node) {
        this.visitChildren(node);
    };

    protected void visit(IfElseStatementASTNode node) {
        this.visitChildren(node);
    };

    ///////////////////////////////////////////////////////////////////////////

    protected void visit(ExpressionASTNode node) {

        if (node instanceof AssignmentExpressionASTNode)
            this.visit((AssignmentExpressionASTNode) node);

        else if (node instanceof BinaryExpressionASTNode)
            this.visit((BinaryExpressionASTNode) node);

        else if (node instanceof UnaryExpressionASTNode)
            this.visit((UnaryExpressionASTNode) node);

        else if (node instanceof FunctionExpressionASTNode)
            this.visit((FunctionExpressionASTNode) node);

        else if (node instanceof OperandASTNode)
            this.visit((OperandASTNode) node);

        /*   else if (node instanceof ExpressionASTNode)
            this.visit(node);*/

        return;
    }

    protected void visit(AssignmentExpressionASTNode node) {
        this.visitChildren(node);
    };

    protected void visit(BinaryExpressionASTNode node) {
        this.visitChildren(node);
    };

    protected void visit(UnaryExpressionASTNode node) {
        this.visitChildren(node);
    };

    protected void visit(FunctionExpressionASTNode node) {
        this.visitChildren(node);
    };

    /////////////////////////////////////////////////////////////////////////

    protected void visit(OperandASTNode node) {

        if (node instanceof ConcreteOperandASTNode)
            this.visit((ConcreteOperandASTNode) node);

        else if (node instanceof BareOperandASTNode)
            this.visit((BareOperandASTNode) node);

        else if (node instanceof LiteralOperandASTNode)
            this.visit((LiteralOperandASTNode) node);

        else if (node instanceof MetaOperandASTNode)
            this.visit((MetaOperandASTNode) node);

        else if (node instanceof ScalarSubscriptOperandASTNode)
            this.visit((ScalarSubscriptOperandASTNode) node);

        else if (node instanceof RangeSubscriptOperandASTNode)
            this.visit((RangeSubscriptOperandASTNode) node);

        return;
    };

    protected void visit(ConcreteOperandASTNode node) {

        if (node instanceof VariableOperandASTNode) {
            this.visit((VariableOperandASTNode) node);
        }

        else if (node instanceof ImmediateOperandASTNode) {
            this.visit((ImmediateOperandASTNode) node);
        }
    }

    protected void visit(BareOperandASTNode node) {
        this.visitChildren(node);
    }

    protected void visit(LiteralOperandASTNode node) {
        this.visitChildren(node);
    }

    protected void visit(MetaOperandASTNode node) {
        this.visitChildren(node);
    }

    protected void visit(VariableOperandASTNode node) {
        this.visitChildren(node);
    }

    protected void visit(ImmediateOperandASTNode node) {
        this.visitChildren(node);
    }

    protected void visit(ScalarSubscriptOperandASTNode node) {
        this.visitChildren(node);
    };

    protected void visit(RangeSubscriptOperandASTNode node) {
        this.visitChildren(node);
    };

    /////////////////////////////////////////////////////////////////////////

    protected void visit(OperatorASTNode node) {
        this.visitChildren(node);
    };
}
