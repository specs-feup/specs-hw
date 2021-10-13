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
 
package pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.expr;

import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNodeType;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.operand.OperandASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.operand.OperandASTNodeSide;

public class AssignmentExpressionASTNode extends ExpressionASTNode {

    private AssignmentExpressionASTNode() {
        super(InstructionASTNodeType.AssignmentExpressionNode);
    }

    public AssignmentExpressionASTNode(OperandASTNode target, ExpressionASTNode expr) {
        this();
        this.addChild(target);
        this.addChild(expr);
        target.setSide(OperandASTNodeSide.LeftHandSide);
    }

    @Override
    public String getAsString() {
        return this.getTarget().getAsString() + " = " + this.getExpr().getAsString();
    }

    public OperandASTNode getTarget() {
        return (OperandASTNode) this.getChild(0);
    }

    public ExpressionASTNode getExpr() {
        return (ExpressionASTNode) this.getChild(1);
    }

    @Override
    protected InstructionASTNode copyPrivate() {
        return new AssignmentExpressionASTNode();
    }
}
