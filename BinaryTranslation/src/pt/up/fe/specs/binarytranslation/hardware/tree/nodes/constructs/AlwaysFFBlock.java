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

import java.util.List;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;

public class AlwaysFFBlock extends HardwareBlock {

    // TODO: signal should be of bounded type, like HardwareSignalEdge

    public AlwaysFFBlock(SignalEdge signal) {
        super(HardwareNodeType.AlwaysFF);
        this.addChild(signal);
    }

    public SignalEdge getSignal() {
        return this.getChild(SignalEdge.class, 0);
    }

    public List<HardwareNode> getStatements() {
        return this.getChildren().subList(1, this.getChildren().size());
        // TODO: children should only be statements (or is it expressions???)
    }

    @Override
    public String getAsString() {
        var builder = new StringBuilder();
        builder.append(super.getAsString());
        builder.append("always_ff @ ( ");
        builder.append(this.getSignal().getAsString());
        builder.append(" ) begin\n");

        this.getStatements().forEach(statement -> builder.append("\t" + statement.getAsString() + "\n"));
        builder.append("end\n");
        return builder.toString();
    }

    @Override
    protected AlwaysFFBlock copyPrivate() {
        return new AlwaysFFBlock(this.getSignal());
    }

    @Override
    public AlwaysFFBlock copy() {
        return (AlwaysFFBlock) super.copy();
    }
}
