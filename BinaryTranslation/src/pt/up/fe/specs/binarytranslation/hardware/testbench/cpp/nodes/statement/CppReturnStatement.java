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

import pt.up.fe.specs.binarytranslation.hardware.testbench.cpp.nodes.CppNode;
import pt.up.fe.specs.binarytranslation.hardware.testbench.cpp.nodes.expression.CppExpression;

public class CppReturnStatement extends CppStatement{

    public CppReturnStatement() {
        this.addChild(new CppNullStatement());
    }
    
    public CppReturnStatement(CppExpression ReturnValue) {
        this.setReturnValue(ReturnValue);
    }
    
    public CppReturnStatement setReturnValue(CppExpression ReturnValue) {
        
        this.replaceChild(this.getChild(0), ReturnValue);
        
        return this;
    }
    
    public CppExpression getReturnValue() {
        return (CppExpression) this.getChild(0);
    }
    
    @Override
    public String getAsString() {
        
        StringBuilder builder = new StringBuilder();
        
        builder.append("return ");
        builder.append(this.getReturnValue().getAsString());
        builder.append(";\n");
        
        return builder.toString();
    }

    @Override
    protected CppNode copyPrivate() {
        return new CppReturnStatement(this.getReturnValue());
    }

}
