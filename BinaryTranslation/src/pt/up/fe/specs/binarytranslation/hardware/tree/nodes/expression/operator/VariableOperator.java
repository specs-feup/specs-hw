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

package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.operator;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.IdentifierDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.subscript.OperatorSubscript;

public class VariableOperator extends HardwareOperator {

    // private final VariableDeclaration declaration;
    private final String name;

    /*
     * Auxiliary copy constructor
     */
    private VariableOperator(VariableOperator other) {
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
    public VariableOperator(IdentifierDeclaration namedIdentifier) {
        super(HardwareNodeType.VariableOperator);
        this.name = namedIdentifier.getVariableName();
    }

    @Override
    public String getAsString() {

        var sb = new StringBuilder();
        sb.append(this.name);

        // print any right hand indexing children;
        // TODO: ensure that right hand children are only indexing nodes
        for (var indexes : this.getChildren(OperatorSubscript.class)) {
            sb.append(indexes.getAsString());
        }

        return sb.toString();
    }

    @Override
    public String getValue() {
        return this.getAsString();
    }

    @Override
    protected VariableOperator copyPrivate() {
        return new VariableOperator(this);
    }

    @Override
    public VariableOperator copy() {
        return (VariableOperator) super.copy();
    }

    /*
     * 
     */
    public VariableOperator addSubscript(OperatorSubscript subs) {
        this.addChild(subs);
        return this;
    }
}
