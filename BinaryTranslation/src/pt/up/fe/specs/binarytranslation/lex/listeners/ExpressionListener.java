/**
 * Copyright 2020 SPeCS.
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

package pt.up.fe.specs.binarytranslation.lex.listeners;

import java.util.Stack;

import org.antlr.v4.runtime.tree.TerminalNode;

import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionBaseListener;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.*;

public class ExpressionListener extends PseudoInstructionBaseListener {

    private Stack<TerminalNode> expressionTokens;

    public ExpressionListener() {
        this.expressionTokens = new Stack<TerminalNode>();
    }

    @Override
    public void enterOperand(OperandContext ctx) {
        this.expressionTokens.push(ctx.getText());
    }

    @Override
    public void enterOperator(OperatorContext ctx) {
        this.expressionTokens.push(ctx.getText());
    }

    public Stack<TerminalNode> getExpressionTokens() {
        return expressionTokens;
    }
}
