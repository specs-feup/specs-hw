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

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;

public class AlwaysBlock extends HardwareBlock {

    public AlwaysBlock() {
        super(HardwareNodeType.Always);
    }

    @Override
    public String getAsString() {
        var builder = new StringBuilder();
        builder.append("always begin\n");
        this.getChildren().forEach(child -> builder.append("\t" + child.getAsString() + "\n"));
        builder.append("end\n");
        return builder.toString();
    }

    @Override
    protected AlwaysBlock copyPrivate() {
        return new AlwaysBlock();
    }

    @Override
    public AlwaysBlock copy() {
        return (AlwaysBlock) super.copy();
    }
}
