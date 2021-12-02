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

package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.reference;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;

public class IndexedSelection extends VariableOperator {

    private IndexedSelection() {
        super(HardwareNodeType.IndexSelection);
    }

    /*
     * Right hand indexing
     */
    public IndexedSelection(VariableOperator namedIdentifier, HardwareOperator index) {
        this();
        this.addChild(namedIdentifier);
        this.addChild(index);
    }

    /*
     * Left hand indexing
     */
    public IndexedSelection(HardwareOperator index, VariableOperator namedIdentifier) {
        this();
        this.addChild(index);
        this.addChild(namedIdentifier);
    }

    public HardwareOperator getIndex() {
        return this.getChild(HardwareOperator.class, 1);
    }

    public VariableOperator getVariable() {
        return this.getChild(VariableOperator.class, 0);
    }

    @Override
    public String getAsString() {
        return this.getVariable().getAsString() + "[" + this.getIndex().getValue() + "]";
    }

    @Override
    protected IndexedSelection copyPrivate() {
        return new IndexedSelection(this.getVariable(), this.getIndex());
    }

    @Override
    public IndexedSelection copy() {
        return (IndexedSelection) super.copy();
    }
}