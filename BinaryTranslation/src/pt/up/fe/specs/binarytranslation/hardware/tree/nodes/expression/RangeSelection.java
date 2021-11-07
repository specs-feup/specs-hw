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
 
package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;

public class RangeSelection extends HardwareExpression {

    private int lower, upper;

    private RangeSelection(int lower, int upper) {
        super();
        this.lower = lower;
        this.upper = upper;
        this.type = HardwareNodeType.RangeSelection;
    }

    public RangeSelection(HardwareNode var, int lower, int upper) {
        this(lower, upper);
        this.addChild(var);
    }

    public RangeSelection(HardwareNode var, int upper) {
        this(var, 0, upper);
    }

    public HardwareNode getVar() {
        return this.getChild(0);
    }

    @Override
    public String getAsString() {
        return this.getVar().getAsString() + "[" + (this.upper - 1) + ":" + this.lower + "]";
    }

    @Override
    protected HardwareNode copyPrivate() {
        return new RangeSelection(this.lower, this.upper);
    }
}
