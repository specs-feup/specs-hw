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

package pt.up.fe.specs.binarytranslation.instruction.ast.passes;

import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.operand.MetaOperandASTNode;

public class MetaFieldFetcherPass extends InstructionASTListener {

    private List<MetaOperandASTNode> metaFields;
    private InstructionASTNode root;

    public MetaFieldFetcherPass(InstructionASTNode root) {
        metaFields = new ArrayList<MetaOperandASTNode>();
    }

    public List<MetaOperandASTNode> getMetaFields() {
        this.visit(root);
        return metaFields;
    }

    @Override
    protected void visit(MetaOperandASTNode node) {
        metaFields.add(node); //
    }
}
