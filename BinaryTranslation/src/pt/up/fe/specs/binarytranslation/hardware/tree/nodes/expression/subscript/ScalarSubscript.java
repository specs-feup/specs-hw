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

package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.subscript;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.operator.HardwareOperator;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.operator.ImmediateOperator;

public class ScalarSubscript extends OperatorSubscript {

    /*
     * Empty node (useful for copying)
     */
    private ScalarSubscript() {
        super(HardwareNodeType.IndexSelection);
    }

    /*
     * Operator can be an @ImmediateOperator or an @IdentifierReference
     */
    public ScalarSubscript(HardwareOperator idx) {
        this();
        this.addChild(idx);
    }

    /*
     * Helper to pass primitive integer index
     */
    public ScalarSubscript(int idx) {
        this(new ImmediateOperator(idx, 32));
    }

    public HardwareOperator getIndex() {
        return this.getChild(HardwareOperator.class, 0);
    }

    @Override
    protected ScalarSubscript copyPrivate() {
        return new ScalarSubscript(); // NOTE: children are copied by super.copy()
    }

    @Override
    public ScalarSubscript copy() {
        return (ScalarSubscript) super.copy();
    }

    @Override
    public String getAsString() {
        return "[" + this.getIndex().getValue() + "]";
    }
}
