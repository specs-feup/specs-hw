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

package pt.up.fe.specs.crispy.ast.expression;

import pt.up.fe.specs.crispy.ast.HardwareNode;
import pt.up.fe.specs.crispy.ast.HardwareNodeType;
import pt.up.fe.specs.crispy.support.HardwareOperationsInterface;

public abstract class HardwareExpression extends HardwareNode implements HardwareOperationsInterface {

    public HardwareExpression(HardwareNodeType type) {
        super(type);
    }

    /*
     * Evaluate the individual components of the expression
     * and return the appropriate bit width
     */
    public abstract int getWidth();

    /*
     * Evaluate the individual components of the expression
     * and return the wire/reg name
     */
    public abstract String getName();

    @Override
    public HardwareExpression getThis() {
        return this;
    }

    @Override
    public String toContentString() {
        return this.getAsString();
    }

    @Override
    public HardwareExpression copy() {
        return (HardwareExpression) super.copy();
    }
}
