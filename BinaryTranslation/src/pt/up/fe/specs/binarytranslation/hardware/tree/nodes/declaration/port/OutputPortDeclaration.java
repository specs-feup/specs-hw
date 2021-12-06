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

package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.port;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.IdentifierDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.ModulePortDirection;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.WireDeclaration;

public class OutputPortDeclaration extends PortDeclaration {

    public OutputPortDeclaration(String portName, int portWidth) {
        super(portName, portWidth, ModulePortDirection.output);
        this.addChild(new WireDeclaration(portName, portWidth));
    }

    public OutputPortDeclaration(IdentifierDeclaration declared) {
        super(declared.getVariableName(), declared.getVariableWidth(), ModulePortDirection.output);
        this.addChild(declared.copy());
    }

    /*
     * Useful for private copy, to prevent copying of single child node
     */
    private OutputPortDeclaration(OutputPortDeclaration other) {
        super(other.getVariableName(), other.getVariableWidth(), ModulePortDirection.output);
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
