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

package pt.up.fe.specs.crispy.ast.expression.aritmetic;

import pt.up.fe.specs.crispy.ast.HardwareNodeType;
import pt.up.fe.specs.crispy.ast.expression.ABinaryHardwareExpression;
import pt.up.fe.specs.crispy.ast.expression.HardwareExpression;
import pt.up.fe.specs.crispy.ast.expression.operator.HardwareOperator;

public class RightArithmeticShiftExpression extends ABinaryHardwareExpression {

    public RightArithmeticShiftExpression(HardwareExpression varA, HardwareExpression varB) {
        super(">>>", HardwareNodeType.RightArithmeticShiftExpression, varA, varB);
    }

    private RightArithmeticShiftExpression(RightArithmeticShiftExpression other) {
        super(other);
    }

    @Override
    public RightArithmeticShiftExpression getThis() {
        return this;
    }

    @Override
    public int getResultWidth() {
        var leftBits = this.getLeft().getResultWidth();
        var rightop = this.getRight();

        int removedBits = 0;
        if (rightop instanceof HardwareOperator) {
            removedBits = ((HardwareOperator) rightop).getMaxValue();
        } else
            removedBits = rightop.getResultWidth();

        return Math.max(leftBits - removedBits, 1); // leave at least 1 bit for a 0 or 1
    }

    @Override
    protected RightArithmeticShiftExpression copyPrivate() {
        return new RightArithmeticShiftExpression(this);
    }

    @Override
    public RightArithmeticShiftExpression copy() {
        return (RightArithmeticShiftExpression) super.copy();
    }
}