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
 
package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;

public class ParameterDeclaration extends VariableDeclaration {

    private final String parameterName;
    private final int numBits;
    private final String initializer;

    public ParameterDeclaration(String parameterName, int numBits, String initializer) {
        super();
        this.parameterName = parameterName;
        this.numBits = numBits;
        this.initializer = initializer;
        this.type = HardwareNodeType.ParameterDeclaration;
    }

    @Override
    public String getVariableName() {
        return this.parameterName;
    }

    @Override
    public String getAsString() {
        return "parameter [ (" + this.numBits + " - 1) : 0] "
                + this.parameterName + "= {" + this.initializer + "};\n";
    }

    @Override
    protected HardwareNode copyPrivate() {
        return new ParameterDeclaration(this.parameterName, this.numBits, this.initializer);
    }
}
