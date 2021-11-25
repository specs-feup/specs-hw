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
 
package pt.up.fe.specs.binarytranslation.hardware.generation.visitors;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.ModulePortDirection;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.port.PortDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.meta.HardwareAnchorNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.PseudoInstructionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.operand.MetaOperandASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.passes.InstructionASTListener;

public class MetaFieldFetcher extends InstructionASTListener {

    HardwareAnchorNode aux = new HardwareAnchorNode();

    public MetaFieldFetcher() {
        // TODO Auto-generated constructor stub
    }

    // TODO: this is only a quick hack!
    public HardwareNode fetchMetaFields(PseudoInstructionASTNode instNode) {

        try {
            this.visit(instNode);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return aux;
    }

    @Override
    protected void visit(MetaOperandASTNode node) {
        this.aux.addChild(new PortDeclaration(node.getAsString(), 1, ModulePortDirection.output));
    }
}
