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

/*
 * This class is not abstract since its also useful for statements which contain blocks, like for loops and if-elses,
 * which are themselves a type of @HardwareStatement, which contains a @HardwareBlock, but are not a block themselves
 */
public class BeginEndBlock extends HardwareBlock {

    protected String blockName;

    public BeginEndBlock() {
        this(HardwareNodeType.BeginEndBlock, "");
    }

    public BeginEndBlock(String blockName) {
        this(HardwareNodeType.BeginEndBlock, blockName);
    }

    protected BeginEndBlock(HardwareNodeType type, String blockName) {
        super(type);
        this.blockName = blockName;
    }

    @Override
    public HardwareBlock getBody() {
        return this;
    }

    /*
     * Useful for sanity checks (See @ModuleBlock sanityCheck method)
     */
    @Override
    public Integer getID() {
        if (this.blockName.isEmpty())
            return super.getID();
        else
            return this.blockName.hashCode();
    }

    @Override
    public String getAsString() {
        var builder = new StringBuilder();

        if (this.getNumChildren() > 1 || !this.blockName.isBlank()) {
            builder.append("begin ");
            if (!this.blockName.isBlank())
                builder.append(": " + this.blockName);
            builder.append("\n");
            builder.append(super.getAsString());
            builder.append("end\n");

        } else {
            builder.append("\n");
            builder.append(super.getAsString());
        }

        return builder.toString();
    }

    @Override
    public BeginEndBlock copy() {
        return (BeginEndBlock) super.copy();
    }

    @Override
    protected BeginEndBlock copyPrivate() {
        return new BeginEndBlock(this.type, this.blockName);
    }
}
