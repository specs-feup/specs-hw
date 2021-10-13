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

import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNodeType;
import pt.up.fe.specs.binarytranslation.instruction.operand.Operand;

public class ImmediateOperandASTNode extends ConcreteOperandASTNode {

    /*
     * This class represents ONLY operands which are literal values (i.e., numbers)
     * But which NECESSARILY have an Operand attached (i.e., they come from filling an immediate field with a value)
     * This is different from literal number fields in the pseudocode, which DO NOT have an associated ASM field
     */
    public ImmediateOperandASTNode(Operand op) {
        super(InstructionASTNodeType.ImmediateNode, op);
    }

    public Number getValue() {
        return this.op.getDataValue();
    }

    @Override
    protected InstructionASTNode copyPrivate() {
        return new ImmediateOperandASTNode(this.op);
    }
}
