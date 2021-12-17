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

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;

public abstract class AUnaryHardwareExpression extends HardwareExpression {

    protected String expressionOperator;

    /*
     * copy without children
     */
    protected AUnaryHardwareExpression(AUnaryHardwareExpression other) {
        super(other.type);
        this.expressionOperator = other.expressionOperator;
    }

    protected AUnaryHardwareExpression(
            String operator, HardwareNodeType type, HardwareExpression varA) {
        super(type);
        this.expressionOperator = operator;
        this.addChild(varA);
    }

    protected HardwareExpression getOperand() {
        return (HardwareExpression) this.getChild(0);
    }

    @Override
    public AUnaryHardwareExpression getThis() {
        return this;
    }

    @Override
    public String toContentString() {
        return this.expressionOperator;
    }

    @Override
    public String getAsString() {
        // return this.toContentString() + " " + this.getOperand().getAsString();
        return this.toContentString() + this.getOperand().getAsString();
    }
}
