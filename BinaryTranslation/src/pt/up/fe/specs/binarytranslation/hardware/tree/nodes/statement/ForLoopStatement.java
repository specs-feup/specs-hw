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

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.HardwareExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.meta.HardwareAnchorNode;

public class ForLoopStatement extends HardwareStatement {

    public ForLoopStatement(ContinuousStatement initialization, HardwareExpression condition,
            HardwareExpression step) {
        super(HardwareNodeType.ForLoop);
        this.addChild(initialization);
        this.addChild(condition);
        this.addChild(step);
        this.addChild(new HardwareAnchorNode()); // will hold children which are individual statements

    }

    public ContinuousStatement getInitialzationStatement() {
        return (ContinuousStatement) this.getChild(0);
    }

    public HardwareExpression getConditionExpression() {
        return (HardwareExpression) this.getChild(1);
    }

    public HardwareExpression getStepExpression() {
        return (HardwareExpression) this.getChild(2);
    }

    public HardwareAnchorNode getStatementList() {
        return (HardwareAnchorNode) this.getChild(3);
    }

    public void addStatement(HardwareNode statement) {
        this.getStatementList().addChild(statement);
    }

    @Override
    public String getAsString() {

        StringBuilder builder = new StringBuilder();

        builder.append("for(");
        builder.append(this.getInitialzationStatement().getAsString());
        builder.append(this.getConditionExpression().getAsString());
        builder.append(this.getStepExpression().getAsString());
        builder.append(") begin\n");

        this.getStatementList().getChildren().forEach(statement -> builder.append(statement.getAsString() + "\n"));

        builder.append("end\n");

        return builder.toString();
    }

    @Override
    protected ForLoopStatement copyPrivate() {
        return new ForLoopStatement(this.getInitialzationStatement(), this.getConditionExpression(),
                this.getStepExpression());
    }

    @Override
    public ForLoopStatement copy() {
        return (ForLoopStatement) super.copy();
    }
}
