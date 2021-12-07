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

package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.meta;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.IdentifierDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.operator.HardwareOperator;

/**
 * This block should emit nothing, and serve only as an anchor point to all declarations in a HardwareTree
 * 
 * @author nuno
 *
 */
public class DeclarationBlock extends HardwareMetaNode {

    /*
     * Simply indicates the meta-name of this block
     */
    private String blockName;
    private Map<String, HardwareOperator> nameMap; // list of references

    public DeclarationBlock(String blockName) {
        super(HardwareNodeType.DeclarationBlock);
        this.blockName = blockName;
        this.nameMap = new HashMap<String, HardwareOperator>();
        this.addChild(new HardwareCommentNode("Declarations block: " + blockName));
    }

    public String getBlockName() {
        return blockName;
    }

    public HardwareNode addDeclaration(HardwareNode child) {
        var declaration = (IdentifierDeclaration) child;
        this.nameMap.put(declaration.getVariableName(), declaration.getReference());
        return super.addChild(child);
    }

    /*
     * get by name
     */
    public Optional<HardwareOperator> getDeclaration(String name) {
        var ref = this.nameMap.get(name);
        return Optional.ofNullable(ref);
    }

    @Override
    protected DeclarationBlock copyPrivate() {
        return new DeclarationBlock(this.blockName);
    }

    @Override
    public DeclarationBlock copy() {
        return (DeclarationBlock) super.copy();
    }

    @Override
    public String getAsString() {
        if (this.getNumChildren() > 1)
            return super.getAsString();
        return "";
    }
}
