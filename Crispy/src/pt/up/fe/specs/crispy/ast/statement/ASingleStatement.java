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

package pt.up.fe.specs.crispy.ast.statement;

import pt.up.fe.specs.crispy.ast.HardwareNodeType;
import pt.up.fe.specs.crispy.ast.expression.HardwareExpression;
import pt.up.fe.specs.crispy.ast.expression.operator.VariableOperator;

public abstract class ASingleStatement extends HardwareStatement {

    protected ASingleStatement(HardwareNodeType type) {
        super(type);
    }

    protected ASingleStatement(HardwareNodeType type,
            VariableOperator target, HardwareExpression expression) {
        this(type);
        this.addChild(target);
        this.addChild(expression);
    }

    public VariableOperator getTarget() {
        return this.getChild(VariableOperator.class, 0);
    }

    public HardwareExpression getExpression() {
        return this.getChild(HardwareExpression.class, 1);
    }
}
