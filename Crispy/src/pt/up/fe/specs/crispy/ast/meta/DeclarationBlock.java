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

package pt.up.fe.specs.crispy.ast.meta;

import java.util.HashMap;
import java.util.Map;

import pt.up.fe.specs.crispy.ast.HardwareNode;
import pt.up.fe.specs.crispy.ast.HardwareNodeType;
import pt.up.fe.specs.crispy.ast.declaration.IdentifierDeclaration;
import pt.up.fe.specs.crispy.ast.expression.operator.HardwareOperator;

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

    public HardwareNode addDeclaration(IdentifierDeclaration declaration) {

        var name = declaration.getVariableName();
        var id = declaration.getID();
        if (this.nameMap.containsKey(name))
            throw new RuntimeException(declaration.getClass().getSimpleName() + " with same " +
                    " ID + (" + id + ") already added to DeclarationBlock \"" + this.blockName + "\"");

        this.nameMap.put(name, declaration.getReference());
        return super.addChild(declaration);
    }

    /*
     * get by name
     */
    public HardwareOperator getDeclaration(String name) {
        return this.nameMap.get(name);
        // var ref = this.nameMap.get(name);
        // return (Optional.ofNullable(ref)).get();
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
