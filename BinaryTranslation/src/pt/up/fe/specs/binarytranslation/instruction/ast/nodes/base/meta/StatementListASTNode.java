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
 
package pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.meta;

import java.util.List;

import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNodeType;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.statement.StatementASTNode;

public class StatementListASTNode extends InstructionASTNode {

    private StatementListASTNode() {
        super();
        this.type = InstructionASTNodeType.StatementListNode;
    }

    public StatementListASTNode(List<StatementASTNode> statements) {
        this();
        for (var stat : statements)
            this.addChild(stat);
    }

    @Override
    public String getAsString() {
        var builder = new StringBuilder();

        if (this.getChildren().size() > 1)
            builder.append("{\n");

        for (int i = 1; i < this.getChildren().size(); i++) {
            builder.append(this.getChild(i).getAsString());
        }

        if (this.getChildren().size() > 1)
            builder.append("}\n");

        return builder.toString();
    }

    @Override
    protected InstructionASTNode copyPrivate() {
        return new StatementListASTNode();
    }
}
