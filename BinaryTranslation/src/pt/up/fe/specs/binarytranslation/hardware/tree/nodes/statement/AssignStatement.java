/**
 * Copyright 2020 SPeCS.
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

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.AHardwareNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.HardwareExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.VariableReference;

public class AssignStatement extends AHardwareNode implements HardwareStatement {

    private final VariableReference target;
    private final HardwareExpression expression;

    // are these the correct fields??..
    // No, should be VariableReference!
    // and the HardwareStatement is an expression, which could be complex, or a VariableReference itself
    // therefore VariableReference extends/implements HardwareStatement

    // TODO: classes like AdditionStatement, etc?

    public AssignStatement(VariableReference target, HardwareExpression expression) {
        this.target = target;
        this.expression = expression;
        target.setParent(this);
        expression.setParent(this);
    }

    @Override
    public String getAsString() {
        return "assign " + target.getAsString() + " = " + expression.getAsString() + ";\n";
    }
}
