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

package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.port;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.ModulePortDirection;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.VariableDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.WireDeclaration;

public abstract class PortDeclaration extends VariableDeclaration {

    private final ModulePortDirection direction;

    protected PortDeclaration(String portName, int portWidth, ModulePortDirection direction) {
        super(portName, portWidth, HardwareNodeType.PortDeclaration);
        this.direction = direction;
        this.addChild(new WireDeclaration(portName, portWidth));
    }

    // TODO eventually deprecate this?
    protected PortDeclaration(VariableDeclaration declared, ModulePortDirection direction) {
        super(declared.getVariableName(), declared.getVariableWidth(), HardwareNodeType.PortDeclaration);
        this.direction = direction;
        this.addChild(declared);
    }

    public ModulePortDirection getDirection() {
        return direction;
    }

    @Override
    public String getAsString() {
        return this.direction.toString() + " " + ((VariableDeclaration) this.getChild(0)).getAsString();
    }
}
