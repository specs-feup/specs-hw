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

package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement;

import java.util.List;

import pt.up.fe.specs.binarytranslation.hardware.HardwareInstance;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;

public class ModuleStatement extends HardwareStatement {

    private HardwareInstance moduleInstance;
    private String moduleName;

    /*
     * Maybe use a map to pass the child port - parent module signal relation ?
     */

    public ModuleStatement(HardwareInstance moduleInstance, String moduleName) {
        this.moduleInstance = moduleInstance;
        this.moduleName = moduleName;
    }

    public ModuleStatement(HardwareInstance moduleDefinition, String instanceName, List<HardwareNode> ports)
            throws IllegalArgumentException {
        this(moduleDefinition, instanceName);

        if (ports.size() != this.moduleInstance.getPorts().size()) {
            throw new IllegalArgumentException();
        }

        this.addChildren(ports);
    }

    @Override
    protected HardwareNode copyPrivate() {
        return new ModuleStatement(this.moduleInstance, this.moduleName);
    }

    @Override
    public String getAsString() {

        StringBuilder builder = new StringBuilder();

        builder.append(this.moduleInstance.getName() + " ");
        builder.append(this.moduleName + " (\n");

        var modulePorts = this.moduleInstance.getPorts();
        for (int i = 0; i < modulePorts.size(); i++) {
            builder.append("\t." + modulePorts.get(i).getVariableName() + "("
                    + this.getChild(i).getAsString() + ")");

            if (i != (modulePorts.size() - 1)) {
                builder.append(",");
            }
            builder.append("\n");
        }

        builder.append(");\n");

        return builder.toString();
    }
}
