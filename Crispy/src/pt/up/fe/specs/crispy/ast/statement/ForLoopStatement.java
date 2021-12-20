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

import pt.up.fe.specs.crispy.ast.HardwareNodeType;
import pt.up.fe.specs.crispy.ast.constructs.BeginEndBlock;
import pt.up.fe.specs.crispy.ast.expression.HardwareExpression;

public class ForLoopStatement extends ABlockStatement {

    protected ForLoopStatement(HardwareNodeType type) {
        super(type);
    }

    /*
     * for copying
     */
    private ForLoopStatement() {
        this(HardwareNodeType.ForLoop);
    }

    public ForLoopStatement(
            ProceduralBlockingStatement initialization,
            HardwareExpression condition, HardwareExpression step) {
        this(initialization, condition, step, "");
    }

    public ForLoopStatement(
            ProceduralBlockingStatement initialization,
            HardwareExpression condition, HardwareExpression step, String blockName) {
        super(HardwareNodeType.ForLoop);
        this.addChild(initialization);
        this.addChild(condition);
        this.addChild(step);
        this.addChild(new BeginEndBlock(blockName)); // will hold children which are individual statements
    }

    public ProceduralBlockingStatement getInitialzationStatement() {
        return this.getChild(ProceduralBlockingStatement.class, 0);
    }

    public HardwareExpression getConditionExpression() {
        return this.getChild(HardwareExpression.class, 1);
    }

    public HardwareExpression getStepExpression() {
        return this.getChild(HardwareExpression.class, 2);
    }

    @Override
    protected BeginEndBlock getBeginEndBlock() {
        return this.getChild(BeginEndBlock.class, 3);
    }

    public HardwareStatement addStatement(HardwareStatement statement) {
        return this.getBeginEndBlock().addStatement(statement);
        // already does sanity check (see @HardwareBlockInterface)
    }

    @Override
    public String getAsString() {
        var builder = new StringBuilder();
        builder.append("for(");
        builder.append(this.getInitialzationStatement().getAsString());
        builder.append(this.getConditionExpression().getAsString());
        builder.append(this.getStepExpression().getAsString());
        builder.append(") ");
        builder.append(this.getBeginEndBlock().getAsString());
        return builder.toString();
    }

    @Override
    protected ForLoopStatement copyPrivate() {
        return new ForLoopStatement();
    }

    @Override
    public ForLoopStatement copy() {
        return (ForLoopStatement) super.copy();
    }
}
