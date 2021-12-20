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

package pt.up.fe.specs.crispy.ast.constructs;

import pt.up.fe.specs.crispy.ast.HardwareNode;
import pt.up.fe.specs.crispy.ast.HardwareNodeType;
import pt.up.fe.specs.crispy.ast.expression.operator.VariableOperator;

public abstract class SignalEdge extends HardwareNode {

    private VariableOperator signal;

    protected SignalEdge(HardwareNodeType type, VariableOperator signal) {
        this(type);
        this.addChild(signal);
    }

    /*
     * copy without children
     */
    protected SignalEdge(HardwareNodeType type) {
        super(type);
    }

    public VariableOperator getSignal() {
        return signal;
    }

    protected abstract String getEdge();

    @Override
    public String getAsString() {
        return this.getEdge() + " " + this.getChild(0).getAsString();
    }

    @Override
    public SignalEdge copy() {
        return (SignalEdge) super.copy();
    }
}
