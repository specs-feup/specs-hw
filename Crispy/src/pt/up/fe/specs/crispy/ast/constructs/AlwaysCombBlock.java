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

public class AlwaysCombBlock extends BeginEndBlock {

    public AlwaysCombBlock() {
        this("");
    }

    public AlwaysCombBlock(String blockName) {
        super(HardwareNodeType.AlwaysComb, blockName);
    }

    @Override
    public String getAsString() {
        return "always_comb " + super.getAsString();
    }

    @Override
    protected AlwaysCombBlock copyPrivate() {
        return new AlwaysCombBlock(this.blockName);
    }

    @Override
    public AlwaysCombBlock copy() {
        return (AlwaysCombBlock) super.copy();
    }
}
