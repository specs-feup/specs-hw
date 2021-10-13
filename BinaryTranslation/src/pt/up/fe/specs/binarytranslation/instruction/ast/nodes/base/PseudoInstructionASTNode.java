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
 
package pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base;

import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNodeType;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.statement.StatementASTNode;

public class PseudoInstructionASTNode extends InstructionASTNode {

    private PseudoInstructionASTNode() {
        this.type = InstructionASTNodeType.PseudoInstructionNode;
    }

    public PseudoInstructionASTNode(List<StatementASTNode> statements) {
        this();
        for (var s : statements)
            this.addChild(s);
    }

    @Override
    public String getAsString() {
        String ret = "";
        for (var s : this.getStatements()) {
            ret += s.getAsString() + "\n";
        }
        ret += "\n";
        return ret;
    }

    public List<StatementASTNode> getStatements() {
        var list = new ArrayList<StatementASTNode>();
        for (var c : this.getChildren())
            list.add((StatementASTNode) c);

        return list;
    }

    @Override
    protected InstructionASTNode copyPrivate() {
        return new PseudoInstructionASTNode();
    }
}
