/**
 * Copyright 2023 SPeCS.
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

import pt.up.fe.specs.crispy.ast.declaration.SignedDeclaration;

public class SignedParameterDeclaration extends ParameterDeclaration implements SignedDeclaration {

    public SignedParameterDeclaration(String parameterName, int numBits, String initializer) {
        super(parameterName, numBits, initializer);
    }

    @Override
    public String getAsString() {
        return "signed " + super.getAsString();
    }

    @Override
    protected SignedParameterDeclaration copyPrivate() {
        return new SignedParameterDeclaration(this.getVariableName(), this.getVariableWidth(), this.initializer);
    }

    @Override
    public SignedParameterDeclaration copy() {
        return (SignedParameterDeclaration) super.copy();
    }
}
