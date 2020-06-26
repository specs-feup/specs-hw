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

package pt.up.fe.specs.binarytranslation.instruction.ast.nodes;

public class BinaryExpressionASTNode extends AInstructionASTNode implements ExpressionASTNode {

    private InstructionASTNode left, right;
    private InstructionASTNode operator;

    public BinaryExpressionASTNode(InstructionASTNode left, InstructionASTNode operator, InstructionASTNode right) {
        super();
        this.left = left;
        this.right = right;
        this.operator = operator;
        this.type = InstructionASTNodeType.BinaryExpressionNode;
        this.children.add(this.left);
        this.children.add(this.right);
    }

    @Override
    public String getAsString() {
        return this.left.getAsString() + this.operator.getAsString() + this.right.getAsString();
    }
}
