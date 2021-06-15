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

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.HardwareExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.VariableReference;

public class ContinuousStatement extends SingleStatement {

    private ContinuousStatement() {
        super(HardwareNodeType.ContinuousAssignment);
    }

    public ContinuousStatement(VariableReference target, HardwareExpression expression) {
        super(HardwareNodeType.ContinuousAssignment, target, expression);
    }

    @Override
    public String toContentString() {
        return "assign";
    }

    @Override
    public String getAsString() {
        return this.toContentString() + " "
                + this.getTarget().getAsString() + " = "
                + this.getExpression().getAsString() + ";";
    }

    @Override
    protected HardwareNode copyPrivate() {
        return new ContinuousStatement();
    }
}
