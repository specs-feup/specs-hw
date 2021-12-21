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

package pt.up.fe.specs.crispy.ast.expression.logical;

import pt.up.fe.specs.crispy.ast.HardwareNodeType;
import pt.up.fe.specs.crispy.ast.expression.AUnaryHardwareExpression;
import pt.up.fe.specs.crispy.ast.expression.HardwareExpression;

public class ReplicationExpression extends AUnaryHardwareExpression {

    private int replicateCount;

    public ReplicationExpression(int replicateCount, HardwareExpression varA) {
        super("", HardwareNodeType.LogicalAndExpression, varA);
        this.replicateCount = replicateCount;
    }

    private ReplicationExpression(ReplicationExpression other) {
        super(other);
        this.replicateCount = other.replicateCount;
    }

    @Override
    public String getResultName() {
        return "rep" + this.replicateCount + "_" + this.getOperand().getResultName();
    }

    @Override
    public ReplicationExpression getThis() {
        return this;
    }

    @Override
    public int getResultWidth() {
        return 1;
    }

    @Override
    protected ReplicationExpression copyPrivate() {
        return new ReplicationExpression(this);
    }

    @Override
    public ReplicationExpression copy() {
        return (ReplicationExpression) super.copy();
    }

    @Override
    public String getAsString() {
        return "{" + this.replicateCount + "{" + this.getOperand().getAsString() + "}}";
    }
}
