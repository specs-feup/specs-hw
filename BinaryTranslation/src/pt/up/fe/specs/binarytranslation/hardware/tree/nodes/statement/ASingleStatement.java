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

package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.HardwareExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.reference.VariableReference;

public abstract class ASingleStatement extends AHardwareStatement {

    protected ASingleStatement(HardwareNodeType type) {
        super(type);
    }

    protected ASingleStatement(HardwareNodeType type,
            VariableReference target, HardwareExpression expression) {
        this(type);
        this.addChild(target);
        this.addChild(expression);
    }

    protected VariableReference getTarget() {
        return (VariableReference) this.getChild(0);
    }

    protected HardwareExpression getExpression() {
        return (HardwareExpression) this.getChild(1);
    }
}
