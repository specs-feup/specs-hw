/**
 * Copyright 2020 SPeCS.
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

package pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.expr;

import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNodeType;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.OperatorASTNode;

public class UnaryExpressionASTNode extends ExpressionASTNode {

    private UnaryExpressionASTNode() {
        super(InstructionASTNodeType.UnaryExpressionNode);
    }

    public UnaryExpressionASTNode(OperatorASTNode operator, ExpressionASTNode right) {
        this();
        this.addChild(operator);
        this.addChild(right);
    }

    public OperatorASTNode getOperator() {
        return (OperatorASTNode) this.getChild(0);
    }

    public ExpressionASTNode getRight() {
        return (ExpressionASTNode) this.getChild(1);
    }

    @Override
    public String getAsString() {
        return this.getOperator().getAsString() + this.getRight().getAsString();
    }

    @Override
    protected InstructionASTNode copyPrivate() {
        return new UnaryExpressionASTNode();
    }
}
