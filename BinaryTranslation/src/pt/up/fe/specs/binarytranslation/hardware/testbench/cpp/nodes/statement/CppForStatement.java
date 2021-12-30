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

import java.util.Collection;
import java.util.List;

import pt.up.fe.specs.binarytranslation.hardware.testbench.cpp.nodes.CppNode;
import pt.up.fe.specs.binarytranslation.hardware.testbench.cpp.nodes.CppNullNode;
import pt.up.fe.specs.binarytranslation.hardware.testbench.cpp.nodes.expression.CppExpression;
import pt.up.fe.specs.binarytranslation.hardware.testbench.cpp.nodes.expression.CppNullExpression;

public class CppForStatement extends CppStatement{
    
    public CppForStatement() {
        this.addChildren(List.of(new CppNullStatement(), new CppNullExpression(), new CppNullExpression(), new CppNullStatement()));
    }

    public CppForStatement(CppStatement InitalValueStatement, CppExpression ConditionExpression, CppExpression IncrementExpression) {  
        this.setInitialValue(InitalValueStatement).setCondition(ConditionExpression).setIncrement(IncrementExpression);
    }

    public CppForStatement setInitialValue(CppStatement InitalValueStatement) {
        
        this.replaceChild(this.getChild(0), InitalValueStatement);
        
        return this;
    }
    
    public CppStatement getInitialValue() {
        return (CppStatement) this.getChild(0);
    }
    
    public CppForStatement setCondition(CppExpression ConditionExpression) {
        
        this.replaceChild(this.getChild(1), ConditionExpression);
        
        return this;
    }
    
    public CppExpression getCondition() {
        return (CppExpression) this.getChild(1);
    }
    
    public CppForStatement setIncrement(CppExpression IncrementExpression) {
        
        this.replaceChild(this.getChild(2), IncrementExpression);
        
        return this;
    }
    
    public CppExpression getIncrement() {
        return (CppExpression) this.getChild(2);
    }

    public CppStatement setBody(CppStatement expression) {
        
        this.replaceChild(this.getChild(3), expression);
        
        return this;
    }
    
    public CppStatement getBody(){
        return (CppStatement) this.getChild(3);
    }
    
    @Override
    public String getAsString() {
        
        StringBuilder builder = new StringBuilder();
        
        builder.append("for(");
        
        builder.append(this.getInitialValue().getAsString());
        builder.append(";");
        builder.append(this.getCondition().getAsString());
        builder.append(";");
        builder.append(this.getIncrement().getAsString());
        
        builder.append("){");
        
        builder.append(this.getBody().getAsString());
        
        builder.append("}");
        
        return builder.toString();
    }

    @Override
    protected CppNode copyPrivate() {
        return new CppForStatement(this.getInitialValue(), this.getCondition(), this.getIncrement());
    }
    
    
}
