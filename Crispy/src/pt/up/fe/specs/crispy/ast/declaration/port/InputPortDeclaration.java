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

package pt.up.fe.specs.crispy.ast.declaration.port;

import java.util.Arrays;
import java.util.List;

import pt.up.fe.specs.crispy.ast.declaration.IdentifierDeclaration;
import pt.up.fe.specs.crispy.ast.declaration.wire.WireDeclaration;
import pt.up.fe.specs.crispy.ast.expression.operator.InputPort;

public class InputPortDeclaration extends PortDeclaration {

    public InputPortDeclaration(String portName, int portWidth) {
        this(new WireDeclaration(portName, portWidth));
    }

    public InputPortDeclaration(IdentifierDeclaration declared) {
        this(declared, Arrays.asList(ModulePortProperties.Data));
    }

    protected InputPortDeclaration(IdentifierDeclaration declared, List<ModulePortProperties> properties) {
        super(declared, ModulePortDirection.input, properties);
    }

    /*
     * Useful for private copy, to prevent copying of single child node
     */
    private InputPortDeclaration(InputPortDeclaration other) {
        super(other);
    }

    @Override
    public InputPort getReference() {
        return new InputPort(this);
    }

    @Override
    public InputPortDeclaration copyPrivate() {
        return new InputPortDeclaration(this);
    }

    @Override
    public InputPortDeclaration copy() {
        return (InputPortDeclaration) super.copy();
    }
}
