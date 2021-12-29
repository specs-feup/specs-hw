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

package pt.up.fe.specs.crispy.ast.declaration;

import pt.up.fe.specs.crispy.ast.HardwareNodeType;
import pt.up.fe.specs.crispy.ast.expression.operator.Wire;

public class WireDeclaration extends IdentifierDeclaration {

    public WireDeclaration(String wireName, int numBits) {
        super(wireName, numBits, HardwareNodeType.WireDeclaration);
    }

    @Override
    public String getAsString() {
        if (this.getVariableWidth() == 1) {
            return "wire " + this.getVariableName() + ";";
        }
        return "wire [" + (this.getVariableWidth() - 1) + " : 0] " + this.getVariableName() + ";";
    }

    @Override
    public Wire getReference() {
        return new Wire(this);
    }

    @Override
    protected WireDeclaration copyPrivate() {
        return new WireDeclaration(this.getVariableName(), this.getVariableWidth());
    }

    @Override
    public WireDeclaration copy() {
        return (WireDeclaration) super.copy();
    }
}