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

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;

public class DelayDeclaration extends HardwareStatement {

    private Number value;

    public DelayDeclaration(Number value) {
        super(HardwareNodeType.DelayDeclaration);
        this.value = value;
    }

    @Override
    protected DelayDeclaration copyPrivate() {
        return new DelayDeclaration(this.value);
    }

    @Override
    public DelayDeclaration copy() {
        return (DelayDeclaration) super.copy();
    }

    @Override
    public String getAsString() {
        return "#" + String.valueOf(this.value) + ";";
    }
}
