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
import pt.up.fe.specs.crispy.ast.expression.operator.VInteger;

public class IntegerDeclaration extends IdentifierDeclaration {

    public IntegerDeclaration(String name) {
        super(name, 32, HardwareNodeType.IntegerDeclaration);
    }

    @Override
    public String getAsString() {
        return "integer " + this.getVariableName() + ";";
    }

    @Override
    protected IntegerDeclaration copyPrivate() {
        return new IntegerDeclaration(this.getVariableName());
    }

    @Override
    public VInteger getReference() {
        return new VInteger(this);
    }

    @Override
    public IntegerDeclaration copy() {
        return (IntegerDeclaration) super.copy();
    }
}
