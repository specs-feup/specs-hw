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

import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.HardwareExpression;

public class IfStatement extends HardwareStatement {

    public IfStatement(HardwareExpression expr) {
        super();
        this.addChild(expr);
        this.type = HardwareNodeType.IfStatement;
    }
    
    public void addStatement(HardwareStatement stat) {
        this.addChild(stat);
    }

    public HardwareExpression getCondition() {
        return (HardwareExpression) this.getChild(0);
    }

    public List<HardwareStatement> getStatements() {
      /*  var list = this.getChildren();
        var nlist = new ArrayList<HardwareStatement>();
        for (var c : list)
            nlist.add((HardwareStatement) c);
        return nlist;*/
        return new ArrayList<HardwareStatement>();
    }

    @Override
    public String toContentString() {
        return "if(" + this.getCondition().toContentString() + ")";
    }

    @Override
    public String getAsString() {
        var builder = new StringBuilder();

        builder.append("if(" + this.getCondition().getAsString() + ")");

        var stats = this.getStatements();
        if (stats.size() > 1)
            builder.append(" begin");
        builder.append("\n\t");

        for (var child : stats) {
            builder.append("\t" + child.getAsString() + "\n");
        }

        if (this.getStatements().size() > 1)
            builder.append("end");
        builder.append("\n");

        return builder.toString();
    }

    @Override
    protected HardwareNode copyPrivate() {
        return new IfStatement(this.getCondition());
    }
}
