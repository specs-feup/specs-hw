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

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;

public class WireDeclaration extends VariableDeclaration {

    public WireDeclaration(String wireName, int numBits) {
        super(wireName, numBits);
        this.type = HardwareNodeType.WireDeclaration;
    }

    @Override
    public String getAsString() {
        return "wire [" + (this.getVariableWidth() - 1) + " : 0] " + this.getVariableName() + ";";
    }

    @Override
    protected HardwareNode copyPrivate() {
        return new WireDeclaration(this.getVariableName(), this.getVariableWidth());
    }
}
