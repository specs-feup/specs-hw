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

package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.reference;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;

public class RangedSelection extends VariableOperator {

    private int lower, upper;

    public RangedSelection(VariableOperator namedIdentifier, int lower, int upper) {
        super(HardwareNodeType.RangeSelection);
        this.addChild(namedIdentifier);
        this.lower = lower;
        this.upper = upper;
    }

    public RangedSelection(IdentifierReference var, int upper) {
        this(var, 0, upper);
    }

    public IdentifierReference getVariable() {
        return this.getChild(IdentifierReference.class, 0);
    }

    @Override
    public String getAsString() {
        return this.getVariable().getAsString() + "[" + (this.upper - 1) + ":" + this.lower + "]";
    }

    @Override
    protected RangedSelection copyPrivate() {
        var copyvar = this.getVariable().copy();
        return new RangedSelection(copyvar, this.lower, this.upper);
    }

    @Override
    public RangedSelection copy() {
        return (RangedSelection) super.copy();
    }
}
