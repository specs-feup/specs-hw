/**
 *  Copyright 2021 SPeCS.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package pt.up.fe.specs.binarytranslation.hardware.testbench.cpp.nodes;

import pt.up.fe.specs.binarytranslation.hardware.testbench.cpp.nodes.expression.CppExpression;
import pt.up.fe.specs.binarytranslation.hardware.testbench.cpp.nodes.statement.CppForStatement;
import pt.up.fe.specs.binarytranslation.hardware.testbench.cpp.nodes.statement.CppIfStatement;
import pt.up.fe.specs.binarytranslation.hardware.testbench.cpp.nodes.statement.CppStatement;
import pt.up.fe.specs.binarytranslation.hardware.testbench.cpp.nodes.statement.CppWhileStatement;
import pt.up.fe.specs.util.treenode.ATreeNode;

public abstract class CppNode extends ATreeNode<CppNode>{
    
    public CppNode() {
        super(null);
    }
    
    public abstract String getAsString();
    
    @Override
    protected CppNode getThis() {
        return this;
    }
    
    @Override
    public String toContentString() {
        return this.getAsString();
    }
    
   public CppForStatement _for(CppStatement InitalValueStatement, CppExpression ConditionExpression, CppExpression IncrementExpression) {
        return (CppForStatement) this.addChild(new CppForStatement(InitalValueStatement, ConditionExpression, IncrementExpression));
    }
    
    public CppWhileStatement _while(CppExpression ConditionExpression) {
        return (CppWhileStatement) this.addChild(new CppWhileStatement(ConditionExpression));
    }
    
    public CppIfStatement _if(CppExpression ConditionExpression) {
        return (CppIfStatement) this.addChild(new CppIfStatement(ConditionExpression));
    }
}
