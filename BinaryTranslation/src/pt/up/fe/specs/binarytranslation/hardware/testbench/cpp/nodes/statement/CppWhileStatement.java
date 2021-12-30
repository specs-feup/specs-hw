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

package pt.up.fe.specs.binarytranslation.hardware.testbench.cpp.nodes.statement;

import java.util.List;

import pt.up.fe.specs.binarytranslation.hardware.testbench.cpp.nodes.CppNode;
import pt.up.fe.specs.binarytranslation.hardware.testbench.cpp.nodes.expression.CppExpression;

public class CppWhileStatement extends CppStatement{

    public CppWhileStatement() {
        
        this.addChildren(List.of(new CppNullStatement(), new CppNullStatement()));
        
    }
    
    public CppWhileStatement(CppExpression ConditionExpression) {
        this.setCondition(ConditionExpression);
    }
    
    public CppWhileStatement setCondition(CppExpression ConditionExpression) {
        
        this.replaceChild(this.getChild(0), ConditionExpression);
        
        return this;
    }
    
    public CppExpression getCondition() {
        return (CppExpression) this.getChild(0);
    }
    
    public CppWhileStatement setBody(CppCompoundStatement body) {
        
        this.replaceChild(this.getChild(1), body);
        
        return this;
    }
    
    public CppCompoundStatement getBody() {
        return (CppCompoundStatement) this.getChild(1);
    }
    
    @Override
    public String getAsString() {
        
        StringBuilder builder = new StringBuilder();
        
        builder.append("while(");
        builder.append(this.getCondition().getAsString());
        builder.append(")");
        
        builder.append((this.getBody().getNumChildren() == 0) ? ";\n" : "{\n" + this.getBody().getAsString() + "}\n");
    
        return builder.toString();
    }

    @Override
    protected CppNode copyPrivate() {
        return new CppWhileStatement(this.getCondition());
    }

}
