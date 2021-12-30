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

public class CppIfStatement extends CppStatement{

    public CppIfStatement() {
        this.addChildren(List.of(new CppNullStatement(), new CppNullStatement(), new CppNullStatement()));
    }
    
    public CppIfStatement(CppExpression ConditionExpression) {
        this();
        this.setCondition(ConditionExpression);
    }
    
    public CppIfStatement(CppExpression ConditionExpression, CppStatement ThenStatements, CppStatement ElseStatements) {
        this.setCondition(ConditionExpression).setThen(ThenStatements).setElse(ElseStatements);
    }
    
    public CppIfStatement setCondition(CppExpression ConditionExpression) {
        
        this.replaceChild(this.getChild(0), ConditionExpression);
        
        return this;
    }
    
    public CppExpression getCondition() {
        return (CppExpression) this.getChild(0);
    }
    
    public CppIfStatement _then(CppStatement ThenStatements) {
        return this.setThen(ThenStatements);
    }
    
    public CppIfStatement setThen(CppStatement ThenStatements) {
        
        this.replaceChild(this.getChild(1), ThenStatements);
        
        return this;
    }
    
    public CppStatement getThen() {
        return (CppStatement) this.getChild(1);
    }
    
    public CppIfStatement _else(CppStatement ElseStatements) {
        return this.setElse(ElseStatements);
    }
    
    public CppIfStatement setElse(CppStatement ElseStatements) {
        
        this.replaceChild(this.getChild(2), ElseStatements);
        
        return this;
    }
    
    public CppStatement getElse() {
        return (CppStatement) this.getChild(2);
    }
    
    @Override
    public String getAsString() {
        
        StringBuilder builder = new StringBuilder();
        
        builder.append("if(");
        builder.append(this.getCondition().getAsString());
        builder.append("){\n");
        builder.append(this.getThen().getAsString());
        builder.append("} ");
        
        if(!(this.getElse() instanceof CppNullStatement)) {
            builder.append("else {\n");
            builder.append(this.getElse().getAsString());
            builder.append("}\n");
        }

        return builder.toString();
    }

    @Override
    protected CppNode copyPrivate() {
        return new CppIfStatement(this.getCondition(), this.getThen(), this.getElse());
    }

}
