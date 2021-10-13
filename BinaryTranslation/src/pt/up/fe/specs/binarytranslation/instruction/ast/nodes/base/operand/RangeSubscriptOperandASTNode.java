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
 
package pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.operand;

import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNodeType;

public class RangeSubscriptOperandASTNode extends SubscriptOperandASTNode {

    private int loidx, hiidx;

    // TODO: make idx a type of node
    private RangeSubscriptOperandASTNode(int loidx, int hiidx) {
        super(InstructionASTNodeType.RangeSubscriptASTNode);
        this.loidx = loidx;
        this.hiidx = hiidx;
    }

    public RangeSubscriptOperandASTNode(OperandASTNode operand, int loidx, int hiidx) {
        super(InstructionASTNodeType.RangeSubscriptASTNode, operand);
        this.loidx = loidx;
        this.hiidx = hiidx;
    }

    @Override
    public String getAsString() {
        return this.getOperand().getAsString() + "[" + this.loidx + ":" + this.hiidx + "]";
    }

    @Override
    public OperandASTNode getOperand() {
        return (OperandASTNode) this.getChild(0);
    }

    public int getLoidx() {
        return loidx;
    }

    public int getHiidx() {
        return hiidx;
    }

    @Override
    protected InstructionASTNode copyPrivate() {
        return new RangeSubscriptOperandASTNode(this.getLoidx(), this.getHiidx());
    }
}
