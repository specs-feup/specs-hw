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

package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.selection;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.HardwareExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.reference.VariableReference;

public class IndexSelection extends HardwareExpression {

    private IndexSelection() {
        super(HardwareNodeType.IndexSelection);
    }

    public IndexSelection(HardwareNode var, VariableReference index) {
        this();
        this.addChild(index);
        this.addChild(var);
    }

    public HardwareNode getIndex() {
        return this.getChild(0);
    }

    public HardwareNode getVar() {
        return this.getChild(1);
    }

    @Override
    public String getAsString() {
        return this.getVar().getAsString() + "[" + this.getIndex().getAsString() + "]";
    }

    @Override
    protected HardwareNode copyPrivate() {
        return new IndexSelection(this.getVar(), (VariableReference) this.getIndex());
    }
}