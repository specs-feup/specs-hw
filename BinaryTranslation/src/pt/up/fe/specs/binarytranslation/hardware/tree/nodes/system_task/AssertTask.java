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

package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.system_task;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.HardwareExpression;

public class AssertTask extends HardwareNode{

    public AssertTask(HardwareExpression expression) {
        this.addChild(expression);
    }

    public HardwareExpression getExpression() {
        return (HardwareExpression) this.getChild(0);
    }
    
    @Override
    protected HardwareNode copyPrivate() {
        return new AssertTask(this.getExpression());
    }
    
    @Override
    public String getAsString() {
        StringBuilder builder = new StringBuilder();
        
        builder.append("$assert(");
        builder.append(this.getExpression().getAsString());
        builder.append(");");
        
        return builder.toString();
    }
    
}
