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

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;

import pt.up.fe.specs.binarytranslation.instruction.ast.InstructionASTGenerator;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionLexer;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.PseudoInstructionContext;
import pt.up.fe.specs.binarytranslation.lex.listeners.TreeDumper;
import pt.up.fe.specs.util.treenode.utils.DottyGenerator;

public class TestLex {

    final static List<String> testStatements;
    static {
        var statements = new ArrayList<String>();

        // basics
        statements.add("RD;");
        statements.add("RD = RA;");
        statements.add("RD = RA + 2;");
        statements.add("RD = RA + -2;");
        statements.add("RD = RA + 4.123;");
        statements.add("RD = RA + -4.123;");
        statements.add("RD = RA + RB;");
        statements.add("RD = RA - RB;");
        statements.add("RD = RA * RB;");
        statements.add("RD = RA / RB;");
        statements.add("RD = RA << RB;");
        statements.add("RD = RA >> RB;");
        statements.add("RD = RA >>> RB;");
        statements.add("RD = RA & RB;");
        statements.add("RD = RA | RB;");
        statements.add("RD = RA ^ RB;");
        statements.add("RD = RA && RB;");
        statements.add("RD = RA || RB;");

        // metafield and built-ins
        statements.add("$pc = $pc + 2;");
        statements.add("RD = $pc + sext(IMM);");

        // subscripts
        statements.add("RD = RA[4];");
        statements.add("RD = RA[2:4];");

        // if
        statements.add("if(RA > 0) RA = 2;");
        statements.add("if(RA > 0) {RA = 2;}");
        statements.add("if(RA > 0) RA = 2; else RA = 4;");
        statements.add("if(RA > 0) {RA = 2;} else {RA = 4;}");
        statements.add("if(RA > 0) {RA = 2;} else {RA = RD[2:4];}");
        statements.add("if(RA > 0) {RA = 2;} else {RA = sext(RD[2:4]);}");

        testStatements = statements;
    };

    @Test
    public void testParseAndTreePrint() {
        for (var s : testStatements) {
            System.out.println("-----------");
            _testParseAndTreePrint(s);
            System.out.println("-----------");
        }
    }

    @Test
    public void testASTConvert() {
        for (var s : testStatements) {
            System.out.println("-----------");
            _testASTConvert(s);
            System.out.println("-----------");
        }
    }

    @Test
    public void testSimpleAST() {
        _testASTConvert("RD = RA + RB + getCarry();");
    }

    private PseudoInstructionContext getTreeRoot(String statement) {

        // create a lexer that feeds off of input CharStream
        var lexer = new PseudoInstructionLexer(CharStreams.fromString(statement));

        // create a buffer of tokens pulled from the lexer
        var tokens = new CommonTokenStream(lexer);

        // create a parser that feeds off the tokens buffer
        var parser = new PseudoInstructionParser(tokens);

        // begin parsing at rule start
        var tree = parser.pseudoInstruction();
        return tree;
    }

    // SEE FULL EXAMPLE AT: https://gist.github.com/mattmcd/5425206

    private void _testParseAndTreePrint(String statement) {

        // begin parsing at rule start
        var tree = getTreeRoot(statement);

        // show parse tree in console
        // System.out.println(tree.toStringTree(parser));

        // walk
        var dumper = new TreeDumper();
        var walker = new ParseTreeWalker();
        walker.walk(dumper, tree);
    }

    private void _testASTConvert(String statement) {

        // begin parsing at rule start
        var tree = getTreeRoot(statement);

        // generate ast
        var astgenerator = new InstructionASTGenerator();
        var ast = astgenerator.visit(tree);

        // System.out.println(ast.getAsString());
        // System.out.println(ast.toString());
        System.out.println(DottyGenerator.buildDotty(ast));
    }
}
