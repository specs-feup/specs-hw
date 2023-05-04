/**
 * Copyright 2023 SPeCS.
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
import pt.up.fe.specs.crispy.ast.block.BeginEndBlock;
import pt.up.fe.specs.crispy.ast.block.HardwareBlock;
import pt.up.fe.specs.crispy.ast.expression.operator.Immediate;

public class CaseItem extends ABlockStatement {

    protected CaseItem(HardwareNodeType type) {
        super(type);
    }

    /*
     * for copying
     */
    private CaseItem() {
        this(HardwareNodeType.CaseItem);
    }

    public Immediate getCaseItemValue() {
        return this.getChild(Immediate.class, 0);
    }

    @Override
    protected BeginEndBlock getBeginEndBlock() {
        return this.getChild(BeginEndBlock.class, 1);
    }

    public CaseItem(Immediate caseValue) {
        super(HardwareNodeType.CaseItem);
        this.addChild(caseValue);
        this.addChild(new BeginEndBlock()); //
    }

    // TODO: move to parent class??
    public HardwareBlock addStatement(HardwareStatement statement) {
        this.getBeginEndBlock()._do(statement);
        return this.getBeginEndBlock();
        // already does sanity check (see @HardwareBlockInterface)
    }

    @Override
    public String getAsString() {
        var builder = new StringBuilder();
        builder.append(this.getCaseItemValue().getAsString());
        builder.append(" : ");
        builder.append(this.getBeginEndBlock().getAsString());
        return builder.toString();
    }

    @Override
    protected CaseItem copyPrivate() {
        return new CaseItem();
    }

    @Override
    public CaseItem copy() {
        return (CaseItem) super.copy();
    }
}
