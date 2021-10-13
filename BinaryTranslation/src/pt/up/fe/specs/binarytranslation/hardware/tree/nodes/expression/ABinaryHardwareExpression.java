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

public abstract class ABinaryHardwareExpression extends HardwareExpression {

    protected String expressionOperator;

    protected ABinaryHardwareExpression(String operator, HardwareNodeType type) {
        super();
        this.expressionOperator = operator;
        this.type = type;
    }

    protected ABinaryHardwareExpression(String operator,
            HardwareNodeType type, HardwareExpression varA, HardwareExpression varB) {
        this(operator, type);
        this.addChild(varA);
        this.addChild(varB);
    }

    protected HardwareExpression getLeft() {
        return (HardwareExpression) this.getChild(0);
    }

    protected HardwareExpression getRight() {
        return (HardwareExpression) this.getChild(1);
    }

    @Override
    public String toContentString() {
        return this.expressionOperator;
    }

    @Override
    public String getAsString() {
        return this.getLeft().getAsString() + " " + this.toContentString() + " " + this.getRight().getAsString();
    }
}
