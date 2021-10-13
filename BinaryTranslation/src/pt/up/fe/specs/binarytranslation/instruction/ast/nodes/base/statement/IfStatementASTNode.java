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
 
package pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.statement;

import java.util.List;

import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNodeType;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.expr.ExpressionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.meta.StatementListASTNode;

public class IfStatementASTNode extends StatementASTNode {

    /*
     * Super call
     */
    protected IfStatementASTNode(InstructionASTNodeType type) {
        super(type);
    }

    /*
     * Normal public constructor
     */
    public IfStatementASTNode(ExpressionASTNode condition,
            List<StatementASTNode> statements) {
        this(InstructionASTNodeType.IfStatementNode, condition, statements);
    }

    /*
     * Supports either IfStatementASTNode or IfElseStatementASTNode construction
     */
    protected IfStatementASTNode(InstructionASTNodeType type, ExpressionASTNode condition,
            List<StatementASTNode> statements) {

        // base constructor
        this(type);

        // condition
        this.addChild(condition);

        // if clauses
        this.addChild(new StatementListASTNode(statements));
    }

    @Override
    public String getAsString() {
        var builder = new StringBuilder();
        builder.append("if (" + this.getChild(0).getAsString() + ") ");
        builder.append(this.getStatements().getAsString());
        return builder.toString();
    }

    public ExpressionASTNode getCondition() {
        return (ExpressionASTNode) this.getChild(0);
    }

    public StatementListASTNode getStatements() {
        return (StatementListASTNode) this.getChild(1);
    }

    @Override
    protected InstructionASTNode copyPrivate() {
        return new IfStatementASTNode(InstructionASTNodeType.IfStatementNode);
    }
}
