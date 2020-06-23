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

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.TerminalNode;

import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionBaseListener;

public class AllNodesListener extends PseudoInstructionBaseListener {

    /*
     * All terminal nodes in the Rule
     */
    private Stack<TerminalNode> allNodes;

    public AllNodesListener() {
        this.allNodes = new Stack<TerminalNode>();
    }

    /*
    @Override
    public void enterEveryRule(ParserRuleContext ctx) {
        System.out.println(ctx.getClass().getTypeName());
    }
    */
    @Override
    public void visitTerminal(TerminalNode node) {
        this.allNodes.push(node);
    }

    /*
     * Get terminal nodes of this tree
     */
    public Stack<TerminalNode> getAllNodes() {
        return allNodes;
    }
}
