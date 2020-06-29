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

package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.AHardwareNode;

public class VariableReference extends AHardwareNode implements HardwareStatement {

    private final String varName;

    public VariableReference(String varName) {
        super();
        this.varName = varName;
    }

    @Override
    public String getAsString() {
        return this.varName;
    }
}
