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

public class VariableOperandASTNode extends ConcreteOperandASTNode {

    /*
     * Quick hack to simplify SSA...
     */
    private String TransformedOperandName;

    /*
     * Nodes of this type represent operands which could be: 
     * "r6" or "imma", i.e., some fields are immediate value fields, but treated as inputs to module
     */
    public VariableOperandASTNode(Operand op) {
        super(InstructionASTNodeType.VariableNode, op);
        this.TransformedOperandName = op.getRepresentation().replace("<", "").replace(">", "").replace("[", "")
                .replace("]", "");
        // clean symbolic prefix/suffix if any
        // TODO: this needs some serious work....
    }

    @Override
    public String getAsString() {
        return this.TransformedOperandName;
    }

    public void setValue(String svalue) {
        this.TransformedOperandName = svalue;
    }

    @Override
    protected InstructionASTNode copyPrivate() {
        return new VariableOperandASTNode(this.op);
    }
}
