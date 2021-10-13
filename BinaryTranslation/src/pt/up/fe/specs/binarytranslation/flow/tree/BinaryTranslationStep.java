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
 
package pt.up.fe.specs.binarytranslation.flow.tree;

import pt.up.fe.specs.util.treenode.ATreeNode;

public abstract class BinaryTranslationStep extends ATreeNode<BinaryTranslationStep> {

    protected BinaryTranslationStepType type;

    public BinaryTranslationStep() {
        super(null);
    }

    public BinaryTranslationStepType getType() {
        return this.type;
    }

    @Override
    public BinaryTranslationStep getThis() {
        return this;
    }

    @Override
    public String toContentString() {
        return this.toString();
    }

    @Override
    protected BinaryTranslationStep copyPrivate() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * Execute this step (Default visits kids)
     */
    public void execute() {
        for (var c : this.getChildren()) {
            c.execute();
        }
    };
}
