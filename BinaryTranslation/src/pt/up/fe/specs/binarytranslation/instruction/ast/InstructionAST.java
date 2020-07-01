package pt.up.fe.specs.binarytranslation.instruction.ast;

import org.antlr.v4.runtime.tree.ParseTree;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.passes.InstructionASTListener;

public class InstructionAST {

    private InstructionASTNode rootnode;
    private ParseTree parseTree;
    private Instruction inst;

    public InstructionAST(Instruction inst) {
        this.inst = inst;
        this.parseTree = inst.getPseudocode().getTree();
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
}
