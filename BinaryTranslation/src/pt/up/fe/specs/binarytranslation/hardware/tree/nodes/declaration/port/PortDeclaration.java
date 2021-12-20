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

import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.IdentifierDeclaration;

public abstract class PortDeclaration extends IdentifierDeclaration {

    // TODO: list of port properties
    // isClock()
    // isReset()
    // is... Handshake??

    private final ModulePortDirection direction;
    private final List<ModulePortProperties> properties;

    /*
    protected PortDeclaration(
            String portName, 
            int portWidth, 
            ModulePortDirection direction,
            List<ModulePortProperties> properties) {
        super(portName, portWidth, HardwareNodeType.PortDeclaration);
        this.direction = direction;
        this.properties = properties;
    }*/

    protected PortDeclaration(IdentifierDeclaration declared,
            ModulePortDirection direction, List<ModulePortProperties> properties) {

        super(declared.getVariableName(),
                declared.getVariableWidth(),
                HardwareNodeType.PortDeclaration);
        this.direction = direction;
        this.properties = properties;
        this.addChild(declared.copy());
    }

    protected PortDeclaration(PortDeclaration other) {
        this(other.getIdentifier().copy(), other.getDirection(), other.cloneProperties());
    }

    protected IdentifierDeclaration getIdentifier() {
        return this.getChild(IdentifierDeclaration.class, 0);
    }

    protected List<ModulePortProperties> cloneProperties() {
        var ret = new ArrayList<ModulePortProperties>();
        for (var p : this.properties)
            ret.add(p);
        return ret;
    }

    public ModulePortDirection getDirection() {
        return direction;
    }

    public boolean isInput() {
        return this.direction == ModulePortDirection.input;
    }

    public boolean isOutput() {
        return this.direction == ModulePortDirection.output;
    }

    public boolean isClock() {
        return this.properties.contains(ModulePortProperties.Clock);
    }

    public boolean isReset() {
        return this.properties.contains(ModulePortProperties.Reset);
    }

    public boolean isData() {
        return this.properties.contains(ModulePortProperties.Data);
    }

    @Override
    public String getAsString() {
        return this.direction.toString() + " " + this.getChild(IdentifierDeclaration.class, 0).getAsString();
    }

    @Override
    protected abstract PortDeclaration copyPrivate();

    @Override
    public PortDeclaration copy() {
        return (PortDeclaration) super.copy();
    }
}
