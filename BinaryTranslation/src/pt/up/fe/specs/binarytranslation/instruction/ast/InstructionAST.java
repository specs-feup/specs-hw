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
 
package pt.up.fe.specs.binarytranslation.instruction.ast;

import org.antlr.v4.runtime.tree.ParseTree;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.passes.InstructionASTListener;

public class InstructionAST {

    private InstructionASTNode rootnode;
    private ParseTree parseTree;
    private Instruction inst;

    // TODO: list of applied passes in order
    // private List<InstructionASTPassType> passesApplied;

    public InstructionAST(Instruction inst) {
        this.inst = inst;
        this.parseTree = inst.getPseudocode().getParseTree();
        this.rootnode = (new InstructionASTGenerator()).visit(parseTree);
    }

    public InstructionAST(Instruction inst, ParseTree parseTree, InstructionASTNode rootnode) {
        this.inst = inst;
        this.parseTree = parseTree;
        this.rootnode = rootnode;
    }

    public InstructionASTNode getRootnode() {
        return rootnode;
    }

    public Instruction getInst() {
        return inst;
    }

    public ParseTree getParseTree() {
        return parseTree;
    }

    public void accept(InstructionASTListener listener) {
        listener.visit(this);
    }

    @Override
    public String toString() {
        return this.getRootnode().toString();
    }
}
