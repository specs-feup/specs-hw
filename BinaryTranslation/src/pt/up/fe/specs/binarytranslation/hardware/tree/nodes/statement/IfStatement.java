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

public class IfStatement extends ABlockStatement {

    /*
     * for copying
     */
    private IfStatement() {
        super(HardwareNodeType.IfStatement);
    }

    public IfStatement(HardwareExpression condition) {
        this(condition, "");
    }

    public IfStatement(HardwareExpression condition, String blockName) {
        super(HardwareNodeType.IfStatement);
        this.addChild(condition);
        this.addChild(new BeginEndBlock(blockName));
    }

    public HardwareExpression getCondition() {
        return this.getChild(HardwareExpression.class, 0);
    }

    @Override
    protected BeginEndBlock getBeginEndBlock() {
        return this.getChild(BeginEndBlock.class, 1);
    }

    public HardwareStatement addStatement(HardwareStatement statement) {
        this.getBeginEndBlock().addStatement(statement);
        return statement;
    }

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
