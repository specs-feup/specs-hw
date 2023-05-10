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

package pt.up.fe.specs.crispy.ast.declaration.parameter;

import pt.up.fe.specs.crispy.ast.HardwareNodeType;
import pt.up.fe.specs.crispy.ast.declaration.IdentifierDeclaration;
import pt.up.fe.specs.crispy.ast.expression.operator.Parameter;

public class ParameterDeclaration extends IdentifierDeclaration {

    protected final String initializer;

    public ParameterDeclaration(String parameterName, int numBits, String initializer) {
        super(parameterName, numBits, HardwareNodeType.ParameterDeclaration);
        this.initializer = initializer;
    }

    @Override
    public String getAsString() {
        return "parameter [ (" + this.getVariableWidth() + " - 1) : 0] "
                + this.getVariableName() + "= {" + this.initializer + "};\n";
    }

    @Override
    public Parameter getReference() {
        return new Parameter(this);
    }

    @Override
    protected ParameterDeclaration copyPrivate() {
        return new ParameterDeclaration(this.getVariableName(), this.getVariableWidth(), this.initializer);
    }

    @Override
    public ParameterDeclaration copy() {
        return (ParameterDeclaration) super.copy();
    }
}
