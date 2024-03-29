/**
 * Copyright 2020 SPeCS.
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

public class ContinuousStatement extends ASingleStatement {

    private ContinuousStatement() {
        super(HardwareNodeType.ContinuousAssignment);
    }

    public ContinuousStatement(VariableOperator target, HardwareExpression expression) {
        super(HardwareNodeType.ContinuousAssignment, target, expression);
    }

    @Override
    public String toContentString() {
        return "assign";
    }

    @Override
    public String getAsString() {
        return this.toContentString() + " "
                + this.getTarget().getAsString() + " = "
                + this.getExpression().getAsString() + ";";
    }

    @Override
    protected ContinuousStatement copyPrivate() {
        return new ContinuousStatement();
    }

    @Override
    public ContinuousStatement copy() {
        return (ContinuousStatement) super.copy();
    }
}
