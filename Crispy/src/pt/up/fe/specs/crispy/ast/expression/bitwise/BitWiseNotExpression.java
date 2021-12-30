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

package pt.up.fe.specs.crispy.ast.expression.bitwise;

import pt.up.fe.specs.crispy.ast.HardwareNodeType;
import pt.up.fe.specs.crispy.ast.expression.AUnaryHardwareExpression;
import pt.up.fe.specs.crispy.ast.expression.HardwareExpression;

public class BitWiseNotExpression extends AUnaryHardwareExpression {

    public BitWiseNotExpression(HardwareExpression varA) {
        super("~", HardwareNodeType.BitWiseNotExpression, varA);
    }

    private BitWiseNotExpression(BitWiseNotExpression other) {
        super(other);
    }

    @Override
    public BitWiseNotExpression getThis() {
        return this;
    }

    @Override
    public String getName() {
        return getOperand().getName();
    }

    @Override
    public int getWidth() {
        return this.getOperand().getWidth();
    }

    @Override
    protected BitWiseNotExpression copyPrivate() {
        return new BitWiseNotExpression(this);
    }

    @Override
    public BitWiseNotExpression copy() {
        return (BitWiseNotExpression) super.copy();
    }
}
