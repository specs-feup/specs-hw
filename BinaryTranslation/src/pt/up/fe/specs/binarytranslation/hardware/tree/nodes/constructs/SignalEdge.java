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

package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.constructs;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.operator.VariableOperator;

public abstract class SignalEdge extends HardwareNode {

    private VariableOperator signal;

    public SignalEdge(HardwareNodeType type, VariableOperator signal) {
        super(type);
        this.signal = signal;
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
    protected abstract SignalEdge copyPrivate();

    @Override
    public SignalEdge copy() {
        return (SignalEdge) super.copy();
    }
}
