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

package pt.up.fe.specs.crispy.ast.expression.operator;

import pt.up.fe.specs.crispy.ast.HardwareNodeType;
import pt.up.fe.specs.crispy.ast.expression.HardwareExpression;

public abstract class HardwareOperator extends HardwareExpression {

    protected HardwareOperator(HardwareNodeType type) {
        super(type);
    }

    /*
     * potential max value of this operand, or specific value if immediate
     */
    public int getMaxValue() {
        var thisop = getThis();

        // we know the value
        if (thisop.getType() == HardwareNodeType.ImmediateOperator) {
            var rightvals = ((Immediate) thisop).getValue();
            return Integer.valueOf(rightvals); // TODO return this directly as int
        }

        // we can only know the max
        else {
            var rightval = ((VariableOperator) thisop).getWidth();
            var rightBits = (int) (Math.log(rightval) / Math.log(2));
            return (int) Math.pow(2.0, rightBits) - 1;
        }
    }

    @Override
    public HardwareOperator getThis() {
        return this;
    }

    @Override
    protected abstract HardwareOperator copyPrivate();

    @Override
    public HardwareOperator copy() {
        return (HardwareOperator) super.copy();
    }

    /*
     * Should return identifier names or immediate values
     */
    public abstract String getValue();
}
