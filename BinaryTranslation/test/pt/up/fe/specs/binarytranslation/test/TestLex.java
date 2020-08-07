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

package pt.up.fe.specs.binarytranslation.test;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;

import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionLexer;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser;
import pt.up.fe.specs.binarytranslation.lex.listeners.TreeDumper;

public class TestLex {

    // SEE FULL EXAMPLE AT: https://gist.github.com/mattmcd/5425206

    @Test
    public void testParseAndTreePrint() {

        // create a CharStream that reads from standard input
        // var input = new ANTLRInputStream("RB = RA + sext(IMM[14:10]);");
        var input = new ANTLRInputStream("if(RB) RA = 2;");

        // create a lexer that feeds off of input CharStream
        var lexer = new PseudoInstructionLexer(input);

        // create a buffer of tokens pulled from the lexer
        var tokens = new CommonTokenStream(lexer);

        // create a parser that feeds off the tokens buffer
        var parser = new PseudoInstructionParser(tokens);

        // begin parsing at rule start
        var tree = parser.pseudoInstruction();

        // show AST in console
        System.out.println(tree.toStringTree(parser));

        // walk
        var dumper = new TreeDumper();
        var walker = new ParseTreeWalker();
        walker.walk(dumper, tree);
    }
}
