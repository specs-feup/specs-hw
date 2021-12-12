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

package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.constructs;

import java.util.List;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.HardwareStatement;

public abstract class HardwareBlock extends HardwareNode {

    public HardwareBlock(HardwareNodeType type) {
        super(type);
    }

    @Override
    public String getAsString() {
        return nestContent(super.getAsString());
    }

    /*
     * All block types are supposed to have statements inside;
     * only @ModuleBlock may have other @HardwareBlock s
     */
    public final HardwareStatement addStatement(HardwareStatement statement) {
        this.addChild(statement);
        return statement;
    }

    public final List<HardwareStatement> getStatements() {
        return this.getChildren(HardwareStatement.class);
        // TODO: children should only be statements (or is it expressions???)
    }

    /*public AdditionExpression add(HardwareOperator target, HardwareExpression refA, HardwareExpression refB) {
        var add = Verilog.add(refA, refB);
        return (AdditionExpression) this.addStatement(add);
    }*/

    // TODO: blocks need all types of things like I put in @HardwareModule,
    // namely, addWire, addRregister, and especially addStatement!
    // TODO: move some of that code here, and since @HardwareModule has
    // an internal @ModuleBlock which is a @HardwareBlock, the funcionaliy
    // is kept!
}
