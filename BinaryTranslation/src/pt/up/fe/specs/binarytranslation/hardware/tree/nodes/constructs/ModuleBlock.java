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

package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.constructs;

import java.util.List;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.port.PortDeclaration;

public class ModuleBlock extends HardwareBlock {

    private String moduleName;
    private List<PortDeclaration> ports;
    // dont treat as children to prevent misformatted nesting (?)

    public ModuleBlock(String moduleName) {
        super(HardwareNodeType.ModuleHeader);
        this.moduleName = moduleName;
    }

    public ModuleBlock(ModuleBlock other) {
        super(HardwareNodeType.ModuleHeader);
        this.moduleName = other.moduleName;
        this.ports = other.ports;
    }

    /*
     * used only to create the string port list;
     * NOT TO BE VISITED AS CHILDREN NODES
     */
    public void addPort(PortDeclaration port) {
        this.ports.add(port);
    }

    public String getModuleName() {
        return moduleName;
    }

    @Override
    public String getAsString() {
        var builder = new StringBuilder();
        builder.append("\nmodule " + this.moduleName + "(");

        for (int i = 0; i < this.ports.size(); i++) {
            builder.append(ports.get(i).getVariableName());
            if (i < this.ports.size() - 1)
                builder.append(", ");
        }
        builder.append(");\n");

        // GET ALL NESTED CONTENT IN BODY BLOCK
        builder.append(super.getAsString());

        builder.append("end //" + this.moduleName + "\n");
        return builder.toString();
    }

    @Override
    protected ModuleBlock copyPrivate() {
        return new ModuleBlock(this);
    }

    @Override
    public ModuleBlock copy() {
        return (ModuleBlock) super.copy();
    }
}
