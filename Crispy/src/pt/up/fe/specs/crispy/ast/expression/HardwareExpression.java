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
    public abstract int getResultWidth();

    /*
     * Evaluate the individual components of the expression
     * and return the wire/reg name
     */
    public abstract String getResultName();

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

    // TODO: other shorthandles to create compound expressions

    // TODO: test putting these methods as public inner classes in HardwareNode?
    // then i could do
    // var result = nonBlocking.add(ref1, ref2);
    // and the "nonBlocking.add" syntax wouldnt have to be inside
    // HardwareOperator family, which requires:
    // ref1.nonBlocking(... etc
}
