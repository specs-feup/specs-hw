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

public class SubtractionExpression extends ABinaryHardwareExpression {

    public SubtractionExpression(HardwareExpression varA, HardwareExpression varB) {
        super("-", HardwareNodeType.SubtractionExpression, varA, varB);
    }

    private SubtractionExpression(SubtractionExpression other) {
        super(other);
    }

    @Override
    public SubtractionExpression getThis() {
        return this;
    }

    @Override
    public int getWidth() {
        return Integer.max(this.getLeft().getWidth(), this.getRight().getWidth()) + 1;
    }

    @Override
    protected SubtractionExpression copyPrivate() {
        return new SubtractionExpression(this);
    }

    @Override
    public SubtractionExpression copy() {
        return (SubtractionExpression) super.copy();
    }
}
