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

package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.reference;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.VariableDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.HardwareExpression;

public class VariableReference extends HardwareExpression {

    // private final VariableDeclaration declaration;
    private final String name;

    /*
     * Auxiliary copy constructor
     */
    private VariableReference(VariableReference other) {
        super(other.type);
        this.name = other.name;
    }

    /*
     * This should be the ONLY valid constructor, since
     * we should only be able to reference something we've
     * declared
     * (I've also added a "getReference()" method to
     * any class which inherits from @VariableDeclaration)
     */
    public VariableReference(VariableDeclaration declaration) {
        super(HardwareNodeType.VariableReference);
        this.name = declaration.getVariableName();
    }

    @Override
    public String getAsString() {
        return this.name;
    }

    @Override
    protected HardwareNode copyPrivate() {
        return new VariableReference(this);
    }
}
