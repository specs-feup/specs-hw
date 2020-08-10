package pt.up.fe.specs.binarytranslation.instruction.ast.passes;

import java.util.HashMap;

import pt.up.fe.specs.binarytranslation.instruction.ast.InstructionAST;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.expr.AssignmentExpressionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.operand.BareOperandASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.transformed.ConcreteOperandASTNode;
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

    private int incrementMapValue(String name) {

        int currCount = 0;
        if (this.nameCountMap.containsKey(name)) {
            currCount = this.nameCountMap.get(name) + 1;
        }
        this.nameCountMap.put(name, currCount);
        return currCount;
    }

    private int getMapValue(String name) {

        if (!this.nameCountMap.containsKey(name)) {
            this.nameCountMap.put(name, 0);
        }

        return this.nameCountMap.get(name);
    }

    /*
     * Values can only be changed in assignment expressions
     */
    @Override
    protected void visit(AssignmentExpressionASTNode node) { // throws UndefinedOperandException {

        // go to expression first
        this.visit(node.getExpr());

        // then change target name
        // TODO: exception if operand is not concrete here!
        var target = (ConcreteOperandASTNode) node.getTarget();
        // if( target.getType() != InstructionASTNodeType ...)
        // throw new UndefinedOperandException()

        // if map contains target, get the name
        var currCount = this.incrementMapValue(target.getAsString());
        target.setValue(target.getAsString() + "_" + currCount);
        // changes the symbolic value of this node
        // WARNING, THIS CHANGE GOES ALL THE WAY UP TO THE OPERAND OBJECT IN THE INSTRUCTION OBJECT!
    }

    @Override
    protected void visit(BareOperandASTNode node) {
        // throw new UndefinedOperandException(node.get...);
    }

    @Override
    protected void visit(VariableOperandASTNode node) {

        // if map contains target, get the name
        var currCount = this.getMapValue(node.getAsString());
        node.setValue(node.getAsString() + "_" + currCount);
        // changes the symbolic value of this node
        // WARNING, THIS CHANGE GOES ALL THE WAY UP TO THE OPERAND OBJECT IN THE INSTRUCTION OBJECT!
    }

    /*
    @Override
    protected void visit(ConcreteOperandASTNode node) {
        
    }*/

}
