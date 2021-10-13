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

import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNodeType;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.expr.ExpressionASTNode;

public class PlainStatementASTNode extends StatementASTNode {

    private PlainStatementASTNode() {
        super(InstructionASTNodeType.PlainStatementNode);
    }

    public PlainStatementASTNode(ExpressionASTNode expr) {
        this();
        this.addChild(expr);
    }

    @Override
    public String getAsString() {
        return this.getExpr().getAsString() + ";";
    }

    public ExpressionASTNode getExpr() {
        return (ExpressionASTNode) this.getChild(0);
    }

    @Override
    protected InstructionASTNode copyPrivate() {
        return new PlainStatementASTNode();
    }
}
