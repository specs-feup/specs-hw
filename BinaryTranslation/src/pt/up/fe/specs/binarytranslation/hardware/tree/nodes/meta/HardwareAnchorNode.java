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

package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.meta;

import java.util.function.Function;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;

/**
 * A type of meta node that emits nothing, only serves to anchor other children to a parent. Useful for return of
 * visitors/listeners.
 * 
 * @author nuno
 *
 */
public class HardwareAnchorNode extends HardwareMetaNode {

    // private Function<HardwareNode, String> toStringOverride;

    public HardwareAnchorNode() {
        super(HardwareNodeType.Anchor);
        // this.toStringOverride =
    }

    /* public HardwareAnchorNode(Function<HardwareNode, String> toStringOverride) {
    
    }
    
    @Override
    public String getAsString() {
        return this.toStringOverride.apply(this);
    }*/

    @Override
    protected HardwareAnchorNode copyPrivate() {
        return new HardwareAnchorNode();
    }

    @Override
    public HardwareAnchorNode copy() {
        return (HardwareAnchorNode) super.copy();
    }
}
