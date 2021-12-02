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

package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.bitwise;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.AUnaryHardwareExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.HardwareExpression;

public class BitWiseNotExpression extends AUnaryHardwareExpression {

    public BitWiseNotExpression(HardwareExpression varA) {
        super("~", HardwareNodeType.BitWiseNotExpression, varA);
    }

    @Override
    protected BitWiseNotExpression copyPrivate() {
        return new BitWiseNotExpression(this.getOperand().copy());
    }

    @Override
    public BitWiseNotExpression copy() {
        return (BitWiseNotExpression) super.copy();
    }
}
