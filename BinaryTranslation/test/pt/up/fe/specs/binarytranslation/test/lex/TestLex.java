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

package pt.up.fe.specs.binarytranslation.test.lex;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;

import pt.up.fe.specs.binarytranslation.instruction.ast.InstructionASTGenerator;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionLexer;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser;
import pt.up.fe.specs.binarytranslation.lex.listeners.TreeDumper;

public class TestLex {

    // SEE FULL EXAMPLE AT: https://gist.github.com/mattmcd/5425206

    private void testParseAndTreePrint(String input) {

        // create a CharStream that reads from standard input
        // var input = new ANTLRInputStream("RB = RA + sext(IMM[14:10]);");
        // var input = new ANTLRInputStream("$pc = $pc + sext(IMM);");

        // create a lexer that feeds off of input CharStream
        var lexer = new PseudoInstructionLexer(CharStreams.fromString(input));

        // create a buffer of tokens pulled from the lexer
        var tokens = new CommonTokenStream(lexer);

        // create a parser that feeds off the tokens buffer
        var parser = new PseudoInstructionParser(tokens);

        // begin parsing at rule start
        var tree = parser.pseudoInstruction();

        // show parse tree in console
        System.out.println(tree.toStringTree(parser));

        // walk
        var dumper = new TreeDumper();
        var walker = new ParseTreeWalker();
        walker.walk(dumper, tree);
    }

    @Test
    public void testMetaFieldParse() {
        testParseAndTreePrint("$pc = $pc + sext(IMM);");
    }

    @Test
    public void testLogicalAndFieldParse() {
        testParseAndTreePrint("RD = RA && RB;");
    }

    @Test
    public void testBitwiseAndFieldParse() {
        testParseAndTreePrint("RD = RA & RB;");
    }

    @Test
    public void testSubscript() {
        testParseAndTreePrint("RD = RA[4];");
        testParseAndTreePrint("RD = RA[2:4];");
    }

    @Test
    public void testASTConvert() {

        var input = CharStreams.fromString("$pc = $pc + sext(IMM);");

        // create a lexer that feeds off of input CharStream
        var lexer = new PseudoInstructionLexer(input);

        // create a buffer of tokens pulled from the lexer
        var tokens = new CommonTokenStream(lexer);

        // create a parser that feeds off the tokens buffer
        var parser = new PseudoInstructionParser(tokens);

        // begin parsing at rule start
        var tree = parser.pseudoInstruction();

        // generate ast
        var astgenerator = new InstructionASTGenerator();
        var ast = astgenerator.visit(tree);

        System.out.println(ast.getAsString());
    }
}
