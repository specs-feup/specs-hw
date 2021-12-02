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

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.HardwareExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.reference.ImmediateReference;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.reference.VariableReference;

public class IndexSelection extends HardwareExpression {

    private IndexSelection() {
        super(HardwareNodeType.IndexSelection);
    }

    public IndexSelection(VariableReference var, ImmediateReference index) {
        this();
        this.addChild(index);
        this.addChild(var);
    }

    public ImmediateReference getIndex() {
        return this.getChild(ImmediateReference.class, 0);
    }

    public VariableReference getVar() {
        return this.getChild(VariableReference.class, 1);
    }

    @Override
    public String getAsString() {
        return this.getVar().getAsString() + "[" + this.getIndex().getAsString() + "]";
    }

    @Override
    protected IndexSelection copyPrivate() {
        return new IndexSelection(this.getVar(), this.getIndex());
    }

    @Override
    public IndexSelection copy() {
        return (IndexSelection) super.copy();
    }
}