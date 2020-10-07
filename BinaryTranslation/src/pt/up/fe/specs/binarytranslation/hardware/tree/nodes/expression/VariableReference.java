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

package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;

public class VariableReference extends HardwareExpression {

    // private final VariableDeclaration declaration;
    private final String name;

    public VariableReference(String declaration) {
        super();
        this.name = declaration;
        this.type = HardwareNodeType.VariableReference;
    }

    @Override
    public String getAsString() {
        return this.name;
    }

    @Override
    protected HardwareNode copyPrivate() {
        return new VariableReference(this.name);
    }
}
