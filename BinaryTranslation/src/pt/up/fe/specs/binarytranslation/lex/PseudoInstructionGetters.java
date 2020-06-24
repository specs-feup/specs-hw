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

package pt.up.fe.specs.binarytranslation.lex;

import java.util.Stack;

import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.tree.*;

import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.ExpressionContext;
import pt.up.fe.specs.binarytranslation.lex.listeners.*;

public class PseudoInstructionGetters {

    /*
     * Get names of AsmFields used in the PseudoInstructionContext (or other rule contexts)
     */
    public static Stack<String> getAsmFields(RuleContext ctx) {
        var walker = new ParseTreeWalker();
        var asmListener = new AsmFieldListener();
        walker.walk(asmListener, ctx);
        return asmListener.getAsmFields();
    }

    /*
     * returns a stack of all terminal Nodes in the parse tree
     */
    public static Stack<TerminalNode> getAllNodes(RuleContext ctx) {
        var walker = new ParseTreeWalker();
        var allTokenListener = new AllNodesListener();
        walker.walk(allTokenListener, ctx);
        return allTokenListener.getAllNodes();
    }

    /*
     * 
     
    public static Stack<TerminalNode> getExpressionTokens(ExpressionContext ctx) {
        var walker = new ParseTreeWalker();
        var exprListener = new ExpressionListener();
        walker.walk(exprListener, ctx);
        return exprListener.getExpressionTokens();
    }*/

    /*
     * 
     */
    // TODO: what else???
}
