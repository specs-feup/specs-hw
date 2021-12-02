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

package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.reference.IdentifierReference;

public abstract class IdentifierDeclaration extends HardwareDeclaration {

    private final int width;
    private final String name;

    protected IdentifierDeclaration(String name, int width, HardwareNodeType type) {
        super(type);
        this.name = name;
        this.width = width;
    }

    public final int getVariableWidth() {
        return width;
    }

    public final String getVariableName() {
        return name;
    }

    @Override
    public String toContentString() {
        return getAsString();
    }

    public IdentifierReference getReference() {
        return new IdentifierReference(this);
    }

    @Override
    protected abstract IdentifierDeclaration copyPrivate();

    @Override
    public IdentifierDeclaration copy() {
        return (IdentifierDeclaration) super.copy();
    }
}
