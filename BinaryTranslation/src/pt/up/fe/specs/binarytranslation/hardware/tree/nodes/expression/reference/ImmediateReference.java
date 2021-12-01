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

package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.reference;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.HardwareExpression;

public class ImmediateReference extends HardwareExpression {

    private int width;
    private final Number value;
    // TODO: private final VariableDeclaration declaration;

    /*
     * Note: for now, all immediates are represented as decimals
     */
    public ImmediateReference(Number value, int width) {
        super();
        this.value = value;
        this.width = width;
        this.type = HardwareNodeType.ImmediateReference;
    }

    @Override
    public String getAsString() {
        return Integer.toString(this.width) + "'d" + this.value;
    }

    @Override
    protected HardwareNode copyPrivate() {
        return new ImmediateReference(this.value, this.width);
    }

    public static ImmediateReference Zeroes(int numbits) {
        return new ImmediateReference(0, numbits);
    }

    public static ImmediateReference Ones(int numbits) {
        return new ImmediateReference(1, numbits);
    }
}
