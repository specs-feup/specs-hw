package pt.up.fe.specs.binarytranslation.instruction.ast.passes;

import java.util.HashMap;

import pt.up.fe.specs.binarytranslation.instruction.ast.InstructionAST;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.expr.AssignmentExpressionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.operand.BareOperandASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.transformed.VariableOperandASTNode;

public class ApplySSAPass extends InstructionASTListener {

    // TODO: apply single static assignement to each individual instruction AST of a binary segment graph (BSG)
    // this pass should return a running string map (for ssa), and pass it to the next instruction in the BSG

    /*
     * Map being used for conversion, starts empty, and is reused for all calls of "visit" for any InstructionAST passed
     */
    private HashMap<String, Integer> nameCountMap;

    public ApplySSAPass() {
        this.nameCountMap = new HashMap<String, Integer>();
    }

    @Override
    public void visit(InstructionAST ast) {
        this.visit(ast.getRootnode());
    }

    /*
     * Returns later-most naming count
     */
    public HashMap<String, Integer> getNameCountMap() {
        return nameCountMap;
    }

    /*
     * Values can only be changed in assignment expressions
     */
    @Override
    protected void visit(AssignmentExpressionASTNode node) { // throws UndefinedOperandException {

        // go to expression first (RHS)
        this.visit(node.getExpr());

        // then change target name (LHS)
        this.visit(node.getTarget());
    }

    @Override
    protected void visit(BareOperandASTNode node) { // throws UndefinedOperandException {
        // throw new UndefinedOperandException(node.get...);
    }

    @Override
    protected void visit(VariableOperandASTNode node) {

        // if map contains variable, then handle the mapping accordingly
        var rnw = node.getInstructionOperand().isRead();
        var name = node.getAsString();
        var currCount = 0;

        // no contain and is read
        if (!this.nameCountMap.containsKey(name) && rnw) {
            return; // do nothing
        }

        // no contain and is write
        else if (!this.nameCountMap.containsKey(name) && !rnw) {
            this.nameCountMap.put(name, 0);
        }

        // contain and is read
        else if (this.nameCountMap.containsKey(name) && rnw) {
            currCount = this.nameCountMap.get(name);
        }

        // contain and is write
        else {
            currCount += 1;
            this.nameCountMap.put(name, currCount);
        }

        // new variable name
        node.setValue(node.getAsString() + "_" + currCount);
    }
}
