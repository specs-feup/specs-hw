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
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.constructs.BeginEndBlock;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.HardwareExpression;

public class IfElseStatement extends ABlockStatement {

    /*
     * for copying
     */
    private IfElseStatement() {
        super(HardwareNodeType.IfElseStatement);
    }

    public IfElseStatement(HardwareExpression condition) {
        super(HardwareNodeType.IfElseStatement);
        this.addChild(condition); // Condition of the if/else statement

        // True condition statement list
        this.addChild(new BeginEndBlock()); // will hold children which are individual statements

        // False condition statement list
        this.addChild(new BeginEndBlock()); // will hold children which are individual statements
    }

    public IfElseStatement addIfStatement(HardwareStatement stat) {
        this.getIfStatements().addChild(stat);
        return this;
    }

    public IfElseStatement addElseStatement(HardwareStatement stat) {
        this.getElseStatements().addChild(stat);
        return this;
    }

    public HardwareExpression getCondition() {
        return this.getChild(HardwareExpression.class, 0);
    }

    public BeginEndBlock getIfStatements() {
        return this.getChild(BeginEndBlock.class, 1);
    }

    public BeginEndBlock getElseStatements() {
        return this.getChild(BeginEndBlock.class, 2);
    }

    @Override
    protected BeginEndBlock getBeginEndBlock() {
        return this.getIfStatements();
    }

    @Override
    public String toContentString() {
        return "if(" + this.getCondition().toContentString() + ")";
    }

    @Override
    public String getAsString() {

        var builder = new StringBuilder();
        builder.append("if(" + this.getCondition().getAsString() + ") ");
        builder.append(this.getIfStatements().getAsString());

        if (!this.getElseStatements().getStatements().isEmpty()) {
            builder.append("else ");
            builder.append(this.getElseStatements().getAsString());
        }
        return builder.toString();

        /*
        var builder = new StringBuilder();
        
        builder.append("if(" + this.getCondition().getAsString() + ") begin\n");
        
        this.getIfStatements().forEach(statement -> builder.append("\t\t" + statement.getAsString() + "\n"));
        
        builder.append("\tend\n");
        
        if (!this.getElseStatements().isEmpty()) {
        
            builder.append("\telse begin\n");
        
            this.getElseStatements().forEach(statement -> builder.append("\t\t" + statement.getAsString() + "\n"));
        
            builder.append("\tend \n");
        }
        return builder.toString();*/
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