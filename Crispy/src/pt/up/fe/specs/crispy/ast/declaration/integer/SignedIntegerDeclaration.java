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

package pt.up.fe.specs.crispy.ast.declaration.integer;

import pt.up.fe.specs.crispy.ast.declaration.SignedDeclaration;

public class SignedIntegerDeclaration extends IntegerDeclaration implements SignedDeclaration {

    public SignedIntegerDeclaration(String name) {
        super(name);
    }

    @Override
    public String getAsString() {
        return "signed " + super.getAsString();
    }

    @Override
    protected SignedIntegerDeclaration copyPrivate() {
        return new SignedIntegerDeclaration(this.getVariableName());
    }

    @Override
    public SignedIntegerDeclaration copy() {
        return (SignedIntegerDeclaration) super.copy();
    }
}
