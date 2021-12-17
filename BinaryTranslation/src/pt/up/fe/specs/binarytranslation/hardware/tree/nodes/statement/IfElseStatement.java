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

import java.util.Arrays;
import java.util.List;

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
        this("", condition);
    }

    public IfElseStatement(String blockName, HardwareExpression condition) {
        super(HardwareNodeType.IfElseStatement);
        this.addChild(condition); // Condition of the if/else statement

        // True condition statement list
        this.addChild(new BeginEndBlock(blockName)); // will hold children which are individual statements

        // False condition statement list
        this.addChild(new BeginEndBlock()); // will hold children which are individual statements
    }

    public IfElseStatement(String blockName,
            HardwareExpression condition, List<HardwareStatement> statements) {
        this(blockName, condition);
        for (var s : statements)
            this.then().addStatement(s);
    }

    public IfElseStatement(HardwareExpression condition, List<HardwareStatement> statements) {
        this("", condition, statements);
    }

    public IfElseStatement(String blockName, HardwareExpression condition, HardwareStatement... statement) {
        this(blockName, condition, Arrays.asList(statement));
    }

    public IfElseStatement(HardwareExpression condition, HardwareStatement... statement) {
        this(condition, Arrays.asList(statement));
    }

    /*
    public IfElseStatement addIfStatement(HardwareStatement stat) {
        this.then().addChild(stat);
        return this;
    }
    
    public IfElseStatement addElseStatement(HardwareStatement stat) {
        this.orElse().addChild(stat);
        return this;
    }*/

    public HardwareExpression getCondition() {
        return this.getChild(HardwareExpression.class, 0);
    }

    @Override
    protected BeginEndBlock getBeginEndBlock() {
        return this.getChild(BeginEndBlock.class, 1);
    }

    private BeginEndBlock then() {
        return this.getChild(BeginEndBlock.class, 1);
    }

    public IfElseStatement then(HardwareStatement... statements) {
        return then(Arrays.asList(statements));
    }

    public IfElseStatement then(List<HardwareStatement> statements) {
        for (var s : statements)
            this.then().addStatement(s);

        // return this.then();
        return this; // DELIBERATE, this way i can chain then(...).orElse(...);
    }

    private BeginEndBlock orElse() {
        return this.getChild(BeginEndBlock.class, 2);
    }

    public BeginEndBlock orElse(HardwareStatement... statements) {
        return orElse(Arrays.asList(statements));
    }

    public BeginEndBlock orElse(List<HardwareStatement> statements) {
        for (var s : statements)
            this.orElse().addStatement(s);

        // TODO: on @IfStatement and here, theres no guarantee that the
        // targets of these statements are declared in the parent ModuleBlock

        return this.orElse();
    }

    @Override
    public String toContentString() {
        return "if(" + this.getCondition().toContentString() + ")";
    }

    @Override
    public String getAsString() {

        var builder = new StringBuilder();
        builder.append("if(" + this.getCondition().getAsString() + ") ");
        builder.append(this.then().getAsString());

        if (!this.orElse().getStatements().isEmpty()) {
            builder.append("else ");
            builder.append(this.orElse().getAsString());
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