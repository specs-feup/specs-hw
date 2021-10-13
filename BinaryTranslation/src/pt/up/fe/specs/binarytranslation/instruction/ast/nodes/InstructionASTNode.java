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
 
package pt.up.fe.specs.binarytranslation.instruction.ast.nodes;

import pt.up.fe.specs.util.treenode.ATreeNode;

public abstract class InstructionASTNode extends ATreeNode<InstructionASTNode> {

    protected InstructionASTNodeType type;

    public InstructionASTNode() {
        super(null);
    }

    public InstructionASTNodeType getType() {
        return type;
    }

    public abstract String getAsString();

    @Override
    public InstructionASTNode getThis() {
        return this;
    }

    @Override
    public String toContentString() {
        return this.getAsString();
    }

    /*
    @Override
    protected InstructionASTNode copyPrivate() {
        Constructor<? extends InstructionASTNode> cs = null;
        InstructionASTNode node = null;
    
        try {
            cs = getThis().getClass().getConstructor();
        } catch (NoSuchMethodException | SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    
        try {
            node = cs.newInstance();
    
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    
        return node;
    }*/
}
