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

package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.comparison;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.ABinaryHardwareExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.HardwareExpression;

public class GreaterThanOrEqualsToExpression extends ABinaryHardwareExpression {

    public GreaterThanOrEqualsToExpression(HardwareExpression varA, HardwareExpression varB) {
        super(">=", HardwareNodeType.GreaterThanOrEqualsToExpression, varA, varB);
    }

    private GreaterThanOrEqualsToExpression(GreaterThanOrEqualsToExpression other) {
        super(other);
    }

    @Override
    public int getResultWidth() {
        return 1;
    }

    @Override
    protected GreaterThanOrEqualsToExpression copyPrivate() {
        return new GreaterThanOrEqualsToExpression(this);
    }

    @Override
    public GreaterThanOrEqualsToExpression copy() {
        return (GreaterThanOrEqualsToExpression) super.copy();
    }
}
