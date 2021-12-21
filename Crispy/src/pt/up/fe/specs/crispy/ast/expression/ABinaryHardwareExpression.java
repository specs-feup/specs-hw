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

import pt.up.fe.specs.crispy.ast.HardwareNodeType;

public abstract class ABinaryHardwareExpression extends HardwareExpression {

    protected String expressionOperator;

    /*
     * copy without children
     */
    protected ABinaryHardwareExpression(ABinaryHardwareExpression other) {
        super(other.type);
        this.expressionOperator = other.expressionOperator;
    }

    protected ABinaryHardwareExpression(
            String operator, HardwareNodeType type,
            HardwareExpression varA, HardwareExpression varB) {
        super(type);
        this.expressionOperator = operator;
        this.addChild(varA);
        this.addChild(varB);
    }

    @Override
    public String getResultName() {
        var l = getLeft().getResultName();
        var r = getRight().getResultName();
        var llen = l.length();
        var rlen = r.length();
        return l.substring(0, Math.min(4, llen))
                + "_" + r.substring(0, Math.min(4, rlen));
    }

    protected HardwareExpression getLeft() {
        return (HardwareExpression) this.getChild(0);
    }

    protected HardwareExpression getRight() {
        return (HardwareExpression) this.getChild(1);
    }

    @Override
    public ABinaryHardwareExpression getThis() {
        return this;
    }

    @Override
    public String toContentString() {
        return this.expressionOperator;
    }

    @Override
    public String getAsString() {
        return this.getLeft().getAsString() + " "
                + this.toContentString() + " "
                + this.getRight().getAsString();
    }
}
