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

import java.util.List;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.HardwareExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.meta.HardwareAnchorNode;

public class IfElseStatement extends HardwareStatement {

    /*
     * empty node to hold list of statements
     */
    private HardwareAnchorNode iflist, elselist;

    public IfElseStatement(HardwareExpression condition) {
        super(HardwareNodeType.IfElseStatement);
        this.iflist = new HardwareAnchorNode();
        this.elselist = new HardwareAnchorNode();

        this.addChild(condition); // Condition of the if/else statement

        // True condition statement list
        this.addChild(iflist); // will hold children which are individual statements

        // False condition statement list
        this.addChild(elselist); // will hold children which are individual statements
    }

    public void addIfStatement(HardwareStatement stat) {
        this.iflist.addChild(stat);
    }

    public void addElseStatement(HardwareStatement stat) {
        this.elselist.addChild(stat);
    }

    public HardwareExpression getCondition() {
        return (HardwareExpression) this.getChild(0);
    }

    public List<HardwareStatement> getIfStatements() {
        return iflist.getChildren(HardwareStatement.class);
    }

    public List<HardwareStatement> getElseStatements() {
        return elselist.getChildren(HardwareStatement.class);
    }

    @Override
    public String toContentString() {
        return "if(" + this.getCondition().toContentString() + ")";
    }

    @Override
    public String getAsString() {

        var builder = new StringBuilder();

        builder.append("if(" + this.getCondition().getAsString() + ") begin\n");

        this.getIfStatements().forEach(statement -> builder.append("\t\t" + statement.getAsString() + "\n"));

        builder.append("\tend\n");

        if (!this.getElseStatements().isEmpty()) {

            builder.append("\telse begin\n");

            this.getElseStatements().forEach(statement -> builder.append("\t\t" + statement.getAsString() + "\n"));

            builder.append("\tend \n");
        }
        return builder.toString();
    }

    @Override
    protected IfElseStatement copyPrivate() {
        return new IfElseStatement(this.getCondition());
    }

    @Override
    public IfElseStatement copy() {
        return (IfElseStatement) super.copy();
    }
}