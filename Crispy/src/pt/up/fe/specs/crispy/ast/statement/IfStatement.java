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

package pt.up.fe.specs.crispy.ast.statement;

import java.util.Arrays;
import java.util.List;

import pt.up.fe.specs.crispy.ast.HardwareNodeType;
import pt.up.fe.specs.crispy.ast.constructs.BeginEndBlock;
import pt.up.fe.specs.crispy.ast.expression.HardwareExpression;

public class IfStatement extends ABlockStatement {

    /*
     * for copying
     */
    private IfStatement() {
        super(HardwareNodeType.IfStatement);
    }

    public IfStatement(HardwareExpression condition) {
        this("", condition);
    }

    public IfStatement(String blockName, HardwareExpression condition) {
        super(HardwareNodeType.IfStatement);
        this.addChild(condition);
        this.addChild(new BeginEndBlock(blockName));
    }

    public IfStatement(String blockName,
            HardwareExpression condition, List<HardwareStatement> statements) {
        this(blockName, condition);
        for (var s : statements)
            this.then().addStatement(s);
    }

    public IfStatement(HardwareExpression condition, List<HardwareStatement> statements) {
        this("", condition, statements);
    }

    public IfStatement(String blockName, HardwareExpression condition, HardwareStatement... statement) {
        this(blockName, condition, Arrays.asList(statement));
    }

    public IfStatement(HardwareExpression condition, HardwareStatement... statement) {
        this(condition, Arrays.asList(statement));
    }

    public HardwareExpression getCondition() {
        return this.getChild(HardwareExpression.class, 0);
    }

    @Override
    protected BeginEndBlock getBeginEndBlock() {
        return this.getChild(BeginEndBlock.class, 1);
    }

    public BeginEndBlock then(HardwareStatement... statements) {
        return then(Arrays.asList(statements));
    }

    public BeginEndBlock then(List<HardwareStatement> statements) {
        for (var s : statements)
            this.getBeginEndBlock().addStatement(s);

        return this.getBeginEndBlock();
    }

    /*-
    public HardwareStatement addStatement(HardwareStatement statement) {
        this.getBeginEndBlock().addStatement(statement);
        return statement;
    }*/

    @Override
    public String toContentString() {
        return "if(" + this.getCondition().toContentString() + ")";
    }

    @Override
    public String getAsString() {
        var builder = new StringBuilder();
        builder.append("if(" + this.getCondition().getAsString() + ") ");
        builder.append(this.getBeginEndBlock().getAsString());
        return builder.toString();
    }

    @Override
    protected IfStatement copyPrivate() {
        return new IfStatement();
    }

    @Override
    public IfStatement copy() {
        return (IfStatement) super.copy();
    }
}
