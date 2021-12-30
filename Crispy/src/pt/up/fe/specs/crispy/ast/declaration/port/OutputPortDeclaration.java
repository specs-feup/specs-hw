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
import pt.up.fe.specs.crispy.ast.declaration.WireDeclaration;
import pt.up.fe.specs.crispy.ast.expression.operator.OutputPort;

public class OutputPortDeclaration extends PortDeclaration {

    public OutputPortDeclaration(String portName, int portWidth) {
        this(new WireDeclaration(portName, portWidth));
    }

    public OutputPortDeclaration(IdentifierDeclaration declared) {
        this(declared, Arrays.asList(ModulePortProperties.Data));
    }

    protected OutputPortDeclaration(IdentifierDeclaration declared, List<ModulePortProperties> properties) {
        super(declared, ModulePortDirection.output, properties);
    }

    /*
     * Useful for private copy, to prevent copying of single child node
     */
    private OutputPortDeclaration(OutputPortDeclaration other) {
        super(other);
    }

    @Override
    public OutputPort getReference() {
        return new OutputPort(this);
    }

    @Override
    public OutputPortDeclaration copyPrivate() {
        return new OutputPortDeclaration(this);
    }

    @Override
    public OutputPortDeclaration copy() {
        return (OutputPortDeclaration) super.copy();
    }
}
