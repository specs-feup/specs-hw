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

package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.selection;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.HardwareExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.reference.VariableReference;

public class RangeSelection extends HardwareExpression {

    private int lower, upper;

    public RangeSelection(VariableReference var, int lower, int upper) {
        super(HardwareNodeType.RangeSelection);
        this.addChild(var);
        this.lower = lower;
        this.upper = upper;
    }

    public RangeSelection(VariableReference var, int upper) {
        this(var, 0, upper);
    }

    public VariableReference getVariable() {
        return this.getChild(VariableReference.class, 0);
    }

    @Override
    public String getAsString() {
        return this.getVariable().getAsString() + "[" + (this.upper - 1) + ":" + this.lower + "]";
    }

    @Override
    protected RangeSelection copyPrivate() {
        var copyvar = this.getVariable().copy();
        return new RangeSelection(copyvar, this.lower, this.upper);
    }

    @Override
    public RangeSelection copy() {
        return (RangeSelection) super.copy();
    }
}
