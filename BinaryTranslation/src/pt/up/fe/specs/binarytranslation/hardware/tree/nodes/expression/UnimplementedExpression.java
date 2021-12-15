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
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.task.HardwareErrorMessage;

public class UnimplementedExpression extends HardwareExpression {

    public UnimplementedExpression(HardwareExpression varA, HardwareExpression varB, String operator) {
        super(HardwareNodeType.UnimplementedExpression);
        this.addChild(new HardwareErrorMessage("Unimplemented expression" +
                " type for expression: \"" + varA.getAsString() + " " + operator + " " + varB.getAsString() + "\""));
    }

    public UnimplementedExpression(HardwareExpression varA, String operator) {
        super(HardwareNodeType.UnimplementedExpression);
        this.addChild(new HardwareErrorMessage("Unimplemented expression" +
                " type for expression: \"" + operator + " " + varA.getAsString() + "\""));
    }

    private UnimplementedExpression(HardwareErrorMessage err) {
        super(HardwareNodeType.UnimplementedExpression);
        this.addChild(err);
    }

    @Override
    public UnimplementedExpression getThis() {
        return this;
    }

    @Override
    public String getResultName() {
        return "";
    }

    @Override
    public int getResultWidth() {
        return 0;
    }

    @Override
    public String getAsString() {
        return this.getChild(0).getAsString();
    }

    @Override
    protected UnimplementedExpression copyPrivate() {
        return new UnimplementedExpression(this.getChild(HardwareErrorMessage.class, 0));
    }

    @Override
    public UnimplementedExpression copy() {
        return (UnimplementedExpression) super.copy();
    }
}
