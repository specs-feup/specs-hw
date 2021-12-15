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

public class ParenthesisExpression extends HardwareExpression {

    private ParenthesisExpression() {
        super(HardwareNodeType.ParenthesisExpression);
    }

    public ParenthesisExpression(HardwareExpression inner) {
        this();
        this.addChild(inner);
    }

    @Override
    public ParenthesisExpression getThis() {
        return this;
    }

    @Override
    public String getResultName() {
        return this.getInner().getResultName();
    }

    @Override
    public int getResultWidth() {
        return this.getInner().getResultWidth();
    }

    @Override
    public String toContentString() {
        return "( )";
    }

    @Override
    public String getAsString() {
        return "( " + this.getInner().getAsString() + " )";
    }

    public HardwareExpression getInner() {
        return (HardwareExpression) this.getChild(0);
    }

    @Override
    protected ParenthesisExpression copyPrivate() {
        return new ParenthesisExpression();
    }

    @Override
    public ParenthesisExpression copy() {
        return (ParenthesisExpression) super.copy();
    }
}
