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

package pt.up.fe.specs.crispy.ast.expression.operator.subscript;

import pt.up.fe.specs.crispy.ast.HardwareNodeType;
import pt.up.fe.specs.crispy.ast.expression.operator.HardwareOperator;
import pt.up.fe.specs.crispy.ast.expression.operator.Immediate;

public class RangedSubscript extends OperatorSubscript {

    /*
     * Empty node (useful for copying)
     */
    private RangedSubscript() {
        super(HardwareNodeType.RangeSelection);
    }

    /*
     * Operator can be an @ImmediateOperator or an @IdentifierReference
     */
    public RangedSubscript(HardwareOperator leftidx, HardwareOperator rightidx) {
        this();
        this.addChild(leftidx);
        this.addChild(rightidx);
    }

    /*
     * Helper to pass primitive integer index
     */
    public RangedSubscript(int leftidx, int rightidx) {
        this(new Immediate(leftidx, 32), new Immediate(rightidx, 32));
    }

    @Override
    public RangedSubscript getThis() {
        return this;
    }

    @Override
    public int getWidth() {
        return Math.abs(this.getLeftIndex().getMaxValue() - this.getRightIndex().getMaxValue());
    }

    @Override
    protected RangedSubscript copyPrivate() {
        return new RangedSubscript(); // NOTE: children are copied by super.copy()
    }

    public HardwareOperator getLeftIndex() {
        return this.getChild(HardwareOperator.class, 0);
    }

    public HardwareOperator getRightIndex() {
        return this.getChild(HardwareOperator.class, 1);
    }

    @Override
    public RangedSubscript copy() {
        return (RangedSubscript) super.copy();
    }

    @Override
    public String getAsString() {
        return "[" + this.getLeftIndex().getValue() + ":" + this.getRightIndex().getValue() + "]";
    }
}
