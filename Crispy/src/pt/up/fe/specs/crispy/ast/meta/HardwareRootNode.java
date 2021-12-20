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

package pt.up.fe.specs.crispy.ast.meta;

import pt.up.fe.specs.crispy.ast.HardwareNodeType;

/**
 * This block should emit nothing, and serve only as a classed root node to a HardwareTree
 * 
 * @author nuno
 *
 */
public class HardwareRootNode extends HardwareMetaNode {

    // TODO: add metadata to the root node
    // e.g., used compile flow, origin of binary traces
    // conditions for translation, used passes etc

    public HardwareRootNode() {
        super(HardwareNodeType.Root);
    }

    @Override
    protected HardwareRootNode copyPrivate() {
        return new HardwareRootNode();
    }

    @Override
    public HardwareRootNode copy() {
        return (HardwareRootNode) super.copy();
    }
}
