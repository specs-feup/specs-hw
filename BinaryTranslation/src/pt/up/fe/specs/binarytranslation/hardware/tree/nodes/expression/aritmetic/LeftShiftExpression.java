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

package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.aritmetic;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.ABinaryHardwareExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.HardwareExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.operator.HardwareOperator;

public class LeftShiftExpression extends ABinaryHardwareExpression {

    public LeftShiftExpression(HardwareExpression varA, HardwareExpression varB) {
        super("<<", HardwareNodeType.LeftShiftExpression, varA, varB);
    }

    private LeftShiftExpression(LeftShiftExpression other) {
        super(other);
    }

    @Override
    public int getResultWidth() {
        var leftBits = this.getLeft().getResultWidth();
        var rightop = this.getRight();

        int addedBits = 0;
        if (rightop instanceof HardwareOperator) {
            addedBits = ((HardwareOperator) rightop).getMaxValue();
        } else
            addedBits = rightop.getResultWidth();

        /*
        int addedBits = 0;
        
        // we know the value
        if (rightop.getType() == HardwareNodeType.ImmediateOperator) {
            var rightvals = ((ImmediateOperator) rightop).getValue();
            addedBits = Integer.valueOf(rightvals); // TODO return this directly as int
        }
        
        // we can only know the max
        else {
            var rightval = rightop.getResultWidth();
            var rightBits = (int) (Math.log(rightval) / Math.log(2));
            addedBits = (int) Math.pow(2.0, rightBits) - 1;
        }*/

        return leftBits + addedBits;
    }

    @Override
    protected LeftShiftExpression copyPrivate() {
        return new LeftShiftExpression(this);
    }

    @Override
    public LeftShiftExpression copy() {
        return (LeftShiftExpression) super.copy();
    }
}
