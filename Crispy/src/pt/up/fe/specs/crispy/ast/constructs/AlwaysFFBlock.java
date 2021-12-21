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

import pt.up.fe.specs.crispy.ast.HardwareNodeType;
import pt.up.fe.specs.crispy.ast.declaration.IdentifierDeclaration;
import pt.up.fe.specs.crispy.ast.declaration.RegisterDeclaration;

public class AlwaysFFBlock extends BeginEndBlock {

    private SignalEdge signalEdge;

    public AlwaysFFBlock(SignalEdge signal) {
        this(signal, "");
    }

    public AlwaysFFBlock(SignalEdge signal, String blockName) {
        super(HardwareNodeType.AlwaysFF, blockName);
        this.signalEdge = signal;
    }

    /*
     * only for copying without children
     */
    private AlwaysFFBlock(AlwaysFFBlock other) {
        super(HardwareNodeType.AlwaysFF, other.blockName);
        this.signalEdge = other.signalEdge;
    }

    @Override
    public IdentifierDeclaration resolveNewDeclaration(String newName) {
        return new RegisterDeclaration(newName, 32);
    }

    @Override
    public String getAsString() {
        return "always_ff @ ( " + this.signalEdge.getAsString() + " ) " + super.getAsString();
    }

    @Override
    protected AlwaysFFBlock copyPrivate() {
        return new AlwaysFFBlock(this);
    }

    @Override
    public AlwaysFFBlock copy() {
        return (AlwaysFFBlock) super.copy();
    }
}
