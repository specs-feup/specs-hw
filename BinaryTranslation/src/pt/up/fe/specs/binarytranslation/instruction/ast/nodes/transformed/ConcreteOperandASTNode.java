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
 
package pt.up.fe.specs.binarytranslation.instruction.ast.nodes.transformed;

import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNodeType;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.operand.OperandASTNode;
import pt.up.fe.specs.binarytranslation.instruction.operand.Operand;

public abstract class ConcreteOperandASTNode extends OperandASTNode {

    /**
     * The {@Operand} reference from the {@Instruction} class instance to which the AST of this node is connected to.
     * 
     * This node type is meant to replace an BareOperandASTNode (which has bare information)
     */
    protected Operand op;

    protected ConcreteOperandASTNode(InstructionASTNodeType type, Operand op) {
        super(type);
        this.op = op;
        // TODO: do we want this to be a copy??
    }

    @Override
    public String getAsString() {
        return op.getRepresentation();
    }

    public int getWidth() {
        return this.op.getProperties().getWidth();
    }

    public Operand getInstructionOperand() {
        return op;
    }
}
