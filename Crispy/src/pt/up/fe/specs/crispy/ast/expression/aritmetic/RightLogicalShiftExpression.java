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

public class RightLogicalShiftExpression extends ABinaryHardwareExpression {

    public RightLogicalShiftExpression(HardwareExpression varA, HardwareExpression varB) {
        super(">>", HardwareNodeType.RightLogicalShiftExpression, varA, varB);
    }

    private RightLogicalShiftExpression(RightLogicalShiftExpression other) {
        super(other);
    }

    @Override
    public RightLogicalShiftExpression getThis() {
        return this;
    }

    @Override
    public int getWidth() {
        var leftBits = this.getLeft().getWidth();
        var rightop = this.getRight();

        int removedBits = 0;
        if (rightop instanceof HardwareOperator) {
            removedBits = ((HardwareOperator) rightop).getMaxValue();
        } else
            removedBits = rightop.getWidth();

        return Math.max(leftBits - removedBits, 1); // leave at least 1 bit for a 0 or 1
    }

    @Override
    protected RightLogicalShiftExpression copyPrivate() {
        return new RightLogicalShiftExpression(this);
    }

    @Override
    public RightLogicalShiftExpression copy() {
        return (RightLogicalShiftExpression) super.copy();
    }
}