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

package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.port.PortDeclaration;

public class ModuleHeader extends HardwareDeclaration {

    /*
     * 
     */
    private String moduleName;

    public ModuleHeader(String moduleName) {
        super(HardwareNodeType.ModuleDeclaration);
        this.moduleName = moduleName;
    }

    @Override
    public String getAsString() {
        var builder = new StringBuilder();
        builder.append("\nmodule " + this.moduleName + "(");

        /*
         * Operate under the assumption that only PortDeclarations are added as children...
         */
        var list = this.getChildren(PortDeclaration.class);
        for (int i = 0; i < list.size(); i++) {
            builder.append(list.get(i).getVariableName());
            if (i < list.size() - 1)
                builder.append(", ");
        }
        builder.append(");\n");
        return builder.toString();
    }

    @Override
    protected ModuleHeader copyPrivate() {
        return new ModuleHeader(this.moduleName);
    }

    @Override
    public ModuleHeader copy() {
        return (ModuleHeader) super.copy();
    }
}
