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
 
package pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.expr;

import java.util.List;

import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNodeType;

public class FunctionExpressionASTNode extends ExpressionASTNode {

    private String builtin;

    public FunctionExpressionASTNode(String builtin) {
        super(InstructionASTNodeType.FunctionExpressionASTNode);
        this.builtin = builtin;
    }

    public FunctionExpressionASTNode(String builtin, List<ExpressionASTNode> args) {
        this(builtin);
        for (var arg : args)
            this.addChild(arg);
    }

    @Override
    public String getAsString() {
        String ret = "";
        ret += this.builtin + "(";

        var iterator = this.getChildren().iterator();

        while (iterator.hasNext()) {
            var arg = iterator.next();
            ret += arg.getAsString();
            if (iterator.hasNext())
                ret += ", ";
        }
        return ret + ")";
    }

    public ExpressionASTNode getArgument(int idx) {
        return (ExpressionASTNode) this.getChild(idx);
    }

    @Override
    protected InstructionASTNode copyPrivate() {
        return new FunctionExpressionASTNode(this.builtin);
    }
}
